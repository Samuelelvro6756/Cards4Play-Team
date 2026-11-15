package cards4play;

import cards4play.api.Errores;
import cards4play.api.RutasCompras;
import cards4play.api.RutasProductos;
import cards4play.api.RutasTorneos;
import cards4play.api.RutasUsuarios;
import cards4play.api.Seguridad;
import cards4play.modelo.Torneo;
import cards4play.servicio.Tienda;
import cards4play.servicio.Usuarios;
import cards4play.util.ApiException;
import cards4play.util.Config;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Main {

    /** Usuarios (administrador y clientes), guardados en usuarios.json. */
    public static Usuarios usuarios;

    /** La tienda con su inventario. */
    public static Tienda tienda;

    /** Torneos. TODO(C): cargarlos y guardarlos en torneos.json. */
    public static List<Torneo> torneos = new ArrayList<>();

    public static void main(String[] args) {
        usuarios = new Usuarios();   // crea el administrador por defecto si no existe
        tienda = new Tienda();

        port(Config.puerto());

        // Todas las respuestas son JSON
        before((req, res) -> res.type("application/json"));

        // Autenticación Basic para todo menos /salud
        before((req, res) -> {
            if (req.pathInfo().equals("/salud")) {
                return;
            }
            req.attribute(Seguridad.ATRIBUTO_USUARIO, Seguridad.autenticar(req, usuarios));
        });

        // Salud (público): sirve para probar que el servidor arrancó
        get("/salud", (req, res) -> "{\"estado\":\"ok\"}");

        // Rutas de cada área
        RutasUsuarios.registrar();   // A
        RutasProductos.registrar();  // B
        RutasCompras.registrar();    // C
        RutasTorneos.registrar();    // C

        // Errores con formato uniforme: {"error": "mensaje"}
        notFound((req, res) -> {
            res.type("application/json");
            return Errores.json("Recurso no encontrado");
        });
        exception(ApiException.class, (e, req, res) -> {
            res.status(e.getCodigo());
            res.type("application/json");
            res.body(Errores.json(e.getMessage()));
        });
        exception(Exception.class, (e, req, res) -> {
            e.printStackTrace();
            res.status(500);
            res.type("application/json");
            res.body(Errores.json("Error interno del servidor"));
        });
    }
}
