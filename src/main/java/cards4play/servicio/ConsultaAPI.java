package cards4play.servicio;

import cards4play.util.Config;

/** Consulta la tasa USD -> COP. Si la API falla, usa la tasa por defecto. */
public class ConsultaAPI {

    private String urlApi = "";        // TODO(C): definir la API externa a usar
    private double tasaDefecto = Config.tasaDefecto();
    private int timeoutMs = 3000;
    private String fuenteTasa = "DEFECTO";

    public double consulta() {
        // TODO(C): llamar a la API con timeout. Si falla:
        //   - imprimir en consola: "La API no respondió, por lo que se toma un valor por defecto de X"
        //   - fuenteTasa = "DEFECTO" y devolver tasaDefecto
        //   si responde: fuenteTasa = "API"
        throw new UnsupportedOperationException("TODO");
    }

    public String getFuenteTasa() {
        return fuenteTasa;
    }
}
