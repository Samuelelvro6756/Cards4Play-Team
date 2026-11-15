package cards4play.api;

import cards4play.util.ApiException;
import cards4play.util.Json;
import com.google.gson.JsonParseException;
import spark.Request;

/** Lee el cuerpo JSON de una petición y lo convierte en un objeto. */
public final class Cuerpo {

    private Cuerpo() {}

    /** 400 si el cuerpo está vacío o no es un JSON válido. */
    public static <T> T leer(Request req, Class<T> clase) {
        String texto = req.body();
        if (texto == null || texto.isBlank()) {
            throw ApiException.solicitudInvalida("El cuerpo de la petición está vacío");
        }
        T objeto;
        try {
            objeto = Json.gson().fromJson(texto, clase);
        } catch (JsonParseException e) {
            throw ApiException.solicitudInvalida("El cuerpo no es un JSON válido");
        }
        if (objeto == null) {
            throw ApiException.solicitudInvalida("El cuerpo de la petición está vacío");
        }
        return objeto;
    }
}
