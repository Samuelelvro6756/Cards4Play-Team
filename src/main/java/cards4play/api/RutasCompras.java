package cards4play.api;

import cards4play.modelo.Compra;
import cards4play.servicio.Tienda;
import com.google.gson.Gson;
import static spark.Spark.*;
import cards4play.Main;
import cards4play.util.Json;
import java.util.LinkedHashMap;
import java.util.Map;
import static spark.Spark.get;

public final class RutasCompras {

    private RutasCompras() {}

    public static void registrar(Tienda tienda, Gson gson) {
        

        post("/compras", (req, res) -> {
            res.type("application/json");
            return gson.toJson("Compra registrada exitosamente");
        });


        get("/compras", (req, res) -> {
            res.type("application/json");
            return gson.toJson(tienda.getInventario().listarProductos()); 
        });

        get("/compras/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            return gson.toJson("Buscando compra con ID: " + id);
        });

        get("/tasa", (req, res) -> {
            res.type("application/json");
            double tasa = tienda.getConsultaAPI().consulta();
            return gson.toJson(tasa);
        });
    }
}