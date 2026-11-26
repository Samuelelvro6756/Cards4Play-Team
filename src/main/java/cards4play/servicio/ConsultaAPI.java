package cards4play.servicio;

import cards4play.util.Config;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConsultaAPI {

    private final String urlApi = Config.urlApiTasa();
    private final double tasaDefecto = Config.tasaDefecto();
    private final int timeoutMs = 3000;
    private String fuenteTasa = "DEFECTO";

    public synchronized double consulta() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(timeoutMs))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlApi))
                    .timeout(Duration.ofMillis(timeoutMs))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonObject rates = json.getAsJsonObject("rates");
                if (rates != null && rates.has("COP")) {
                    fuenteTasa = "API";
                    return rates.get("COP").getAsDouble();
                }
            }
        } catch (Exception e) {
            // Si hay timeout, sin internet o error de parseo, se usa la tasa por defecto
        }

        System.out.println("La API no respondió, por lo que se toma un valor por defecto de " + tasaDefecto);
        fuenteTasa = "DEFECTO";
        return tasaDefecto;
    }

    public synchronized String getFuenteTasa() {
        return fuenteTasa;
    }
}
