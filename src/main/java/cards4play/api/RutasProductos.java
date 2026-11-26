package cards4play.api;

import cards4play.Main;
import cards4play.modelo.Accesorio;
import cards4play.modelo.Administrador;
import cards4play.modelo.Booster;
import cards4play.modelo.Carta;
import cards4play.modelo.Cliente;
import cards4play.modelo.Producto;
import cards4play.modelo.ProductoSellado;
import cards4play.modelo.Rareza;
import cards4play.servicio.Inventario;
import cards4play.util.ApiException;
import cards4play.util.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

/** Rutas de productos y boosters. */
public final class RutasProductos {

    private static final Set<String> TIPOS_VALIDOS = Set.of("CARTA", "SELLADO", "ACCESORIO", "BOOSTER");

    private RutasProductos() {}

    private static class NuevoProducto {
        String tipo; // CARTA, SELLADO o ACCESORIO
        String nombre;
        double precioUsd;
        String rareza;
    }

    private static class ActualizarProducto {
        String nombre;
        Double precioUsd;
        String rareza;
    }

    /** Cuerpo esperado en POST /boosters. */
    private static class NuevoBooster {
        String nombre;
        double precioUsd;
    }

    public static void registrar() {
        post("/productos", (req, res) -> {
            Administrador admin = Seguridad.exigirAdmin(req);
            NuevoProducto datos = Cuerpo.leer(req, NuevoProducto.class);
            Producto producto = construir(datos);
            admin.crearProducto(producto);
            refrescarTasa();
            res.status(201);
            return Json.aJson(vista(producto));
        });

        get("/productos", (req, res) -> {
            Seguridad.usuarioActual(req);
            String filtroTipo = req.queryParams("tipo");
            if (filtroTipo != null && !filtroTipo.isBlank()) {
                filtroTipo = filtroTipo.trim().toUpperCase();
                if (!TIPOS_VALIDOS.contains(filtroTipo)) {
                    throw ApiException.solicitudInvalida("Tipo de filtro no válido (CARTA, SELLADO, ACCESORIO o BOOSTER)");
                }
            }
            refrescarTasa();
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Producto p : inventario().listarProductos()) {
                if (filtroTipo == null || filtroTipo.isBlank() || Inventario.tipoDe(p).equals(filtroTipo)) {
                    lista.add(vista(p));
                }
            }
            return Json.aJson(lista);
        });

        get("/productos/:id", (req, res) -> {
            Seguridad.usuarioActual(req);
            Producto p = inventario().buscarPorId(req.params(":id"));
            if (p == null) {
                throw ApiException.noEncontrado("No existe un producto con ese id");
            }
            refrescarTasa();
            return Json.aJson(vista(p));
        });

        put("/productos/:id", (req, res) -> {
            Seguridad.exigirAdmin(req);
            Producto p = inventario().buscarPorId(req.params(":id"));
            if (p == null) {
                throw ApiException.noEncontrado("No existe un producto con ese id");
            }
            if (p instanceof Booster && !((Booster) p).estaDisponible()) {
                throw ApiException.conflicto("No se puede modificar un booster que ya fue vendido");
            }
            ActualizarProducto datos = Cuerpo.leer(req, ActualizarProducto.class);
            if (datos.nombre != null) {
                if (datos.nombre.isBlank()) {
                    throw ApiException.solicitudInvalida("El nombre no puede estar vacío");
                }
                p.setNombre(datos.nombre);
            }
            if (datos.precioUsd != null) {
                if (datos.precioUsd <= 0) {
                    throw ApiException.solicitudInvalida("El precio debe ser mayor que 0");
                }
                p.setPrecioUsd(datos.precioUsd);
            }
            if (datos.rareza != null) {
                if (!(p instanceof Carta)) {
                    throw ApiException.solicitudInvalida("Solo las cartas tienen rareza");
                }
                try {
                    ((Carta) p).setRareza(Rareza.valueOf(datos.rareza.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw ApiException.solicitudInvalida("Rareza no válida (N, UC, R, SR o UR)");
                }
            }
            inventario().guardar();
            refrescarTasa();
            return Json.aJson(vista(p));
        });

        post("/boosters", (req, res) -> {
            Administrador admin = Seguridad.exigirAdmin(req);
            NuevoBooster datos = Cuerpo.leer(req, NuevoBooster.class);

            Booster booster = new Booster(UUID.randomUUID().toString(), datos.nombre, datos.precioUsd);
            inventario().validar(booster);                         // 400 si nombre o precio no sirven
            booster.generarCartas(inventario().listarCartas());    // 409 si no hay cartas
            admin.crearProducto(booster);                          // guarda en productos.json

            refrescarTasa();
            res.status(201);
            return Json.aJson(vista(booster));                     // sin mostrar las cartas
        });

        get("/boosters/disponibles", (req, res) -> {
            Seguridad.usuarioActual(req);
            refrescarTasa();
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Booster b : inventario().listarBoostersDisponibles()) {
                lista.add(vista(b));        // vista() nunca muestra las cartas
            }
            return Json.aJson(lista);
        });

        get("/boosters/mios", (req, res) -> {
            Cliente cliente = Seguridad.exigirCliente(req);
            refrescarTasa();
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Booster b : inventario().listarBoostersDe(cliente.getId())) {
                Map<String, Object> m = vista(b);
                if (b.isAbierto()) {
                    List<Map<String, Object>> contenido = new ArrayList<>();
                    for (Carta c : b.getCartas()) {
                        contenido.add(vista(c));
                    }
                    m.put("cartas", contenido);
                }
                lista.add(m);
            }
            return Json.aJson(lista);
        });

        post("/boosters/:id/abrir", (req, res) -> {
            Cliente cliente = Seguridad.exigirCliente(req);        // 403 si es admin
            Producto p = inventario().buscarPorId(req.params(":id"));
            if (!(p instanceof Booster)) {
                throw ApiException.noEncontrado("No existe un booster con ese id");
            }
            Booster booster = (Booster) p;
            List<Carta> cartas = cliente.abrirBooster(booster);    // 403 ajeno/no vendido, 409 ya abierto
            inventario().guardar();                                // guarda abierto = true
            refrescarTasa();

            List<Map<String, Object>> contenido = new ArrayList<>();
            for (Carta c : cartas) {
                contenido.add(vista(c));
            }
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("id", booster.getId());
            respuesta.put("nombre", booster.getNombre());
            respuesta.put("abierto", booster.isAbierto());
            respuesta.put("fuenteTasa", Main.tienda.getConsultaAPI().getFuenteTasa());
            respuesta.put("cartas", contenido);
            return Json.aJson(respuesta);
        });
    }

    private static Inventario inventario() {
        return Main.tienda.getInventario();
    }

    private static void refrescarTasa() {
        Main.tienda.actualizarPreciosCop();
    }

    private static Producto construir(NuevoProducto d) {
        if (d.tipo == null) {
            throw ApiException.solicitudInvalida("Falta el campo 'tipo' (CARTA, SELLADO o ACCESORIO)");
        }
        String id = UUID.randomUUID().toString();
        switch (d.tipo.toUpperCase()) {
            case "CARTA": {
                if (d.rareza == null) {
                    throw ApiException.solicitudInvalida("Falta la 'rareza' (N, UC, R, SR o UR)");
                }
                Rareza rareza;
                try {
                    rareza = Rareza.valueOf(d.rareza.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw ApiException.solicitudInvalida("Rareza no válida (N, UC, R, SR o UR)");
                }
                return new Carta(id, d.nombre, d.precioUsd, rareza);
            }
            case "SELLADO":
                return new ProductoSellado(id, d.nombre, d.precioUsd);
            case "ACCESORIO":
                return new Accesorio(id, d.nombre, d.precioUsd);
            case "BOOSTER":
                throw ApiException.solicitudInvalida("Los boosters se crean con POST /boosters");
            default:
                throw ApiException.solicitudInvalida("Tipo no válido (CARTA, SELLADO o ACCESORIO)");
        }
    }

    private static Map<String, Object> vista(Producto p) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", p.getId());
        m.put("tipo", Inventario.tipoDe(p));
        m.put("nombre", p.getNombre());
        m.put("precioUsd", p.getPrecioUsd());
        m.put("cop", p.getCop());
        m.put("fuenteTasa", Main.tienda.getConsultaAPI().getFuenteTasa());
        if (p instanceof Carta) {
            m.put("rareza", ((Carta) p).getRareza().name());
        }
        if (p instanceof Booster) {
            Booster b = (Booster) p;
            m.put("vendido", !b.estaDisponible());
            m.put("abierto", b.isAbierto());
        }
        return m;
    }
}
