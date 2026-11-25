package cards4play.api;

import cards4play.modelo.Cliente;
import cards4play.modelo.Torneo;
import cards4play.servicio.Tienda;
import cards4play.util.ApiException;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.UUID;
import static spark.Spark.get;
import static spark.Spark.post;

/** Rutas de C: torneos. */
public final class RutasTorneos {

    private RutasTorneos() {}

    private static class NuevoTorneo {
        String nombre;
        String fecha;       // AAAA-MM-DD
        int cupoMaximo;
    }

    public static void registrar(Tienda tienda, Gson gson) {

        post("/torneos", (req, res) -> {
            Seguridad.exigirAdmin(req);
            NuevoTorneo d = Cuerpo.leer(req, NuevoTorneo.class);
            if (d.nombre == null || d.nombre.isBlank()) {
                throw ApiException.solicitudInvalida("El nombre es obligatorio");
            }
            if (d.cupoMaximo <= 0) {
                throw ApiException.solicitudInvalida("El cupo máximo debe ser mayor que 0");
            }
            LocalDate fecha;
            try {
                fecha = LocalDate.parse(d.fecha);
            } catch (DateTimeParseException | NullPointerException e) {
                throw ApiException.solicitudInvalida("La fecha debe tener el formato AAAA-MM-DD");
            }
            Torneo creado = tienda.crearTorneo(
                    new Torneo(UUID.randomUUID().toString(), d.nombre, fecha, d.cupoMaximo));
            res.status(201);
            return gson.toJson(creado);
        });

        get("/torneos", (req, res) -> gson.toJson(tienda.listarTorneos()));

        post("/torneos/:id/inscripciones", (req, res) -> {
            Cliente cliente = Seguridad.exigirCliente(req);
            tienda.inscribirEnTorneo(req.params(":id"), cliente);
            res.status(201);
            return gson.toJson(Map.of("mensaje", "Inscripción exitosa"));
        });
    }
}