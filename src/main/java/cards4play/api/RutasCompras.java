package cards4play.api;

import cards4play.modelo.Administrador;
import cards4play.modelo.Booster;
import cards4play.modelo.Carta;
import cards4play.modelo.Cliente;
import cards4play.modelo.Compra;
import cards4play.modelo.DetalleCompra;
import cards4play.modelo.Producto;
import cards4play.servicio.Inventario;
import cards4play.servicio.Tienda;
import cards4play.util.ApiException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static spark.Spark.get;
import static spark.Spark.post;

public final class RutasCompras {

    private RutasCompras() {}

    /** Cuerpo esperado en POST /compras: {"productoIds": ["...", "..."]} */
    private static class NuevaCompra {
        List<String> productoIds;
    }

    public static void registrar(Tienda tienda, Gson gson) {

        post("/compras", (req, res) -> {
            Cliente cliente = Seguridad.exigirCliente(req);
            NuevaCompra datos = Cuerpo.leer(req, NuevaCompra.class);
            if (datos.productoIds == null || datos.productoIds.isEmpty()) {
                throw ApiException.solicitudInvalida("Falta la lista 'productoIds'");
            }
            List<Producto> productos = new ArrayList<>();
            for (String id : datos.productoIds) {
                if (id == null || id.isBlank()) {
                    throw ApiException.solicitudInvalida("Todos los ids de productos deben ser válidos");
                }
                Producto p = tienda.getInventario().buscarPorId(id);
                if (p == null) {
                    throw ApiException.noEncontrado("No existe el producto " + id);
                }
                productos.add(p);
            }
            Compra compra = tienda.comprar(cliente, productos);
            res.status(201);
            return gson.toJson(vistaCompra(compra, tienda));
        });

        get("/compras", (req, res) -> {
            var usuario = Seguridad.usuarioActual(req);
            tienda.actualizarPreciosCop();
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Compra c : tienda.listarCompras()) {
                if (usuario instanceof Administrador || c.getClienteId().equals(usuario.getId())) {
                    lista.add(vistaCompra(c, tienda));
                }
            }
            return gson.toJson(lista);
        });

        get("/compras/:id", (req, res) -> {
            var usuario = Seguridad.usuarioActual(req);
            Compra compra = tienda.buscarCompra(req.params(":id"));
            if (compra == null) {
                throw ApiException.noEncontrado("No existe una compra con ese id");
            }
            if (!(usuario instanceof Administrador) && !compra.getClienteId().equals(usuario.getId())) {
                throw ApiException.prohibido("Esta compra no es tuya");
            }
            tienda.actualizarPreciosCop();
            return gson.toJson(vistaCompra(compra, tienda));
        });

        get("/tasa", (req, res) -> {
            Seguridad.usuarioActual(req);
            double tasa = tienda.getConsultaAPI().consulta();
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("usdCop", tasa);
            r.put("fuenteTasa", tienda.getConsultaAPI().getFuenteTasa());
            return gson.toJson(r);
        });
    }

    /**
     * Construye la vista de una compra para el historial:
     * - Si un detalle corresponde a un booster ABIERTO, muestra sus 10 cartas obtenidas.
     * - Si el booster NO ha sido abierto, omite su contenido.
     */
    private static Map<String, Object> vistaCompra(Compra c, Tienda tienda) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", c.getId());
        m.put("clienteId", c.getClienteId());
        m.put("fecha", c.getFecha());
        m.put("tasaUsada", c.getTasaUsada());
        m.put("fuenteTasa", c.getFuenteTasa());
        m.put("totalCop", c.getTotalCop());

        List<Map<String, Object>> detalles = new ArrayList<>();
        for (DetalleCompra d : c.getDetalles()) {
            Map<String, Object> dm = new LinkedHashMap<>();
            dm.put("productoId", d.getProductoId());
            dm.put("nombre", d.getNombre());
            dm.put("cantidad", d.getCantidad());
            dm.put("precioUnitarioCop", d.getPrecioUnitarioCop());

            Producto p = tienda.getInventario().buscarPorId(d.getProductoId());
            if (p != null) {
                dm.put("tipo", Inventario.tipoDe(p));
                if (p instanceof Booster) {
                    Booster b = (Booster) p;
                    dm.put("abierto", b.isAbierto());
                    if (b.isAbierto()) {
                        List<Map<String, Object>> cartas = new ArrayList<>();
                        for (Carta carta : b.getCartas()) {
                            Map<String, Object> cm = new LinkedHashMap<>();
                            cm.put("id", carta.getId());
                            cm.put("nombre", carta.getNombre());
                            cm.put("rareza", carta.getRareza().name());
                            cm.put("precioUsd", carta.getPrecioUsd());
                            cm.put("cop", carta.getCop());
                            cartas.add(cm);
                        }
                        dm.put("cartas", cartas);
                    }
                }
            }
            detalles.add(dm);
        }
        m.put("detalles", detalles);
        return m;
    }
}
