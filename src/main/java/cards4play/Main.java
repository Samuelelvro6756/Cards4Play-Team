package cards4play;

import cards4play.api.Errores;
import cards4play.util.Config;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(Config.puerto());

        before((req, res) -> res.type("application/json"));

        get("/heal", (req, res) -> "{\"estado\":\"ok\"}");

        // TODO(A): crear el administrador por defecto si no existe, y la autenticación Basic (401/403)
        // TODO(A/B/C): registrar las rutas de cada área (clientes, productos, boosters, compras, torneos, tasa)

        notFound((req, res) -> {
            res.type("application/json");
            return Errores.json("Recurso no encontrado");
        });
        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.type("application/json");
            res.body(Errores.json("Error interno del servidor"));
        });
    }
}
