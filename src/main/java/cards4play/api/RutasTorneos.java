package cards4play.api;

import cards4play.modelo.Cliente;
import cards4play.modelo.Torneo;
import cards4play.servicio.Tienda;
import com.google.gson.Gson;
import java.util.Map;
import static spark.Spark.get;
import static spark.Spark.post;

/** Rutas de C: torneos. */
public final class RutasTorneos {

    private RutasTorneos() {}

    public static void registrar(Tienda tienda, Gson gson) {

        post("/torneos", (req, res) -> {
            res.type("application/json");
            Torneo nuevoTorneo = gson.fromJson(req.body(), Torneo.class);
            Torneo creado = tienda.crearTorneo(nuevoTorneo);
            res.status(201);
            return gson.toJson(creado);
        });

        get("/torneos", (req, res) -> {
            res.type("application/json");
            return gson.toJson(tienda.listarTorneos());
        });

        post("/torneos/:id/inscripciones", (req, res) -> {
            res.type("application/json");
            String idTorneo = req.params(":id");
            Cliente cliente = gson.fromJson(req.body(), Cliente.class);

            tienda.inscribirEnTorneo(idTorneo, cliente);
            res.status(201);
            return gson.toJson(Map.of("mensaje", "Inscripción exitosa"));
        });
    }
}