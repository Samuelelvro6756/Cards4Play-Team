package cards4play.api;

import cards4play.modelo.Administrador;
import cards4play.modelo.Cliente;
import cards4play.modelo.Compra;
import cards4play.modelo.Producto;
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
                Producto p = tienda.getInventario().buscarPorId(id);
                if (p == null) {
                    throw ApiException.noEncontrado("No existe el producto " + id);
                }
                productos.add(p);
            }
            Compra compra = tienda.comprar(cliente, productos);
            res.status(201);
            return gson.toJson(compra);
        });

        get("/compras", (req, res) -> {
            var usuario = Seguridad.usuarioActual(req);
            List<Compra> lista = new ArrayList<>();
            for (Compra c : tienda.listarCompras()) {
                if (usuario instanceof Administrador || c.getClienteId().equals(usuario.getId())) {
                    lista.add(c);
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
            return gson.toJson(compra);
        });

        get("/tasa", (req, res) -> {
            double tasa = tienda.getConsultaAPI().consulta();
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("usdCop", tasa);
            r.put("fuenteTasa", tienda.getConsultaAPI().getFuenteTasa());
            return gson.toJson(r);
        });
    }
}