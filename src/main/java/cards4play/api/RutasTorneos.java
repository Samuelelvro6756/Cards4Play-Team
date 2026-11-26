package cards4play.api;

import cards4play.modelo.Administrador;
import cards4play.modelo.Cliente;
import cards4play.modelo.Torneo;
import cards4play.servicio.Tienda;
import cards4play.util.ApiException;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static spark.Spark.get;
import static spark.Spark.post;

/** Rutas de torneos. */
public final class RutasTorneos {

    private RutasTorneos() {}

    private static class NuevoTorneo {
        String nombre;
        String fecha;       // AAAA-MM-DD
        int cupoMaximo;
    }

    public static void registrar(Tienda tienda, Gson gson) {

        post("/torneos", (req, res) -> {
            Administrador admin = Seguridad.exigirAdmin(req);
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
            Torneo creado = admin.crearTorneo(
                    new Torneo(UUID.randomUUID().toString(), d.nombre, fecha, d.cupoMaximo));
            res.status(201);
            return gson.toJson(vistaTorneo(creado));
        });

        get("/torneos", (req, res) -> {
            Seguridad.usuarioActual(req);
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Torneo t : tienda.listarTorneos()) {
                lista.add(vistaTorneo(t));
            }
            return gson.toJson(lista);
        });

        get("/torneos/:id", (req, res) -> {
            Seguridad.usuarioActual(req);
            Torneo torneo = tienda.buscarTorneo(req.params(":id"));
            if (torneo == null) {
                throw ApiException.noEncontrado("No existe un torneo con ese id");
            }
            return gson.toJson(vistaTorneo(torneo));
        });

        post("/torneos/:id/inscripciones", (req, res) -> {
            Cliente cliente = Seguridad.exigirCliente(req);
            String idTorneo = req.params(":id");
            tienda.inscribirEnTorneo(idTorneo, cliente);
            res.status(201);
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("mensaje", "Inscripción exitosa");
            r.put("torneoId", idTorneo);
            r.put("clienteId", cliente.getId());
            return gson.toJson(r);
        });
    }

    private static Map<String, Object> vistaTorneo(Torneo t) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", t.getId());
        m.put("nombre", t.getNombre());
        m.put("fecha", t.getFecha());
        m.put("cupoMaximo", t.getCupoMaximo());
        m.put("cuposDisponibles", t.getCuposDisponibles());
        m.put("inscritos", t.getInscritos());
        return m;
    }
}
