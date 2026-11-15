package cards4play.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Gson compartido por todo el proyecto. Incluye adaptadores para LocalDate y LocalDateTime
 * (sin ellos, Gson falla con las fechas en Java 17 o superior).
 */
public final class Json {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate valor) throws IOException {
                    out.value(valor.toString());
                }

                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    return LocalDate.parse(in.nextString());
                }
            }.nullSafe())
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter out, LocalDateTime valor) throws IOException {
                    out.value(valor.toString());
                }

                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    return LocalDateTime.parse(in.nextString());
                }
            }.nullSafe())
            .setPrettyPrinting()
            .create();

    private Json() {}

    public static Gson gson() {
        return GSON;
    }

    /** Convierte cualquier objeto en el texto JSON de la respuesta. */
    public static String aJson(Object objeto) {
        return GSON.toJson(objeto);
    }
}
