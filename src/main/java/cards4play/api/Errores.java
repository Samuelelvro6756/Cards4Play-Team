package cards4play.api;

import com.google.gson.JsonObject;

/** Formato único de error de toda la API: {"error": "mensaje"}. */
public final class Errores {

    private Errores() {}

    public static String json(String mensaje) {
        JsonObject o = new JsonObject();
        o.addProperty("error", mensaje);
        return o.toString();
    }
}
