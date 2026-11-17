package cards4play.api;

import cards4play.Main;
import cards4play.util.Json;
import java.util.LinkedHashMap;
import java.util.Map;
import static spark.Spark.get;

public final class RutasCompras {

    private RutasCompras() {}

    public static void registrar() {

        get("/tasa", (req, res) -> {
            double tasa = Main.tienda.getConsultaAPI().consulta();
            Map<String, Object> mapa = new LinkedHashMap<>();
            mapa.put("tasa", tasa);
            mapa.put("fuenteTasa", Main.tienda.getConsultaAPI().getFuenteTasa());
            return Json.aJson(mapa);
        });
    }
}