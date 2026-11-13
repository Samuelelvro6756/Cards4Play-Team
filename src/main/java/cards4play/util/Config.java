package cards4play.util;

/** Lee la configuración desde variables de entorno, con valores por defecto. */
public final class Config {

    private Config() {}

    /** Tasa USD->COP que se usa si la API externa no responde (variable TASA_DEFECTO). */
    public static double tasaDefecto() {
        // TODO(equipo): definir el valor real por defecto y documentarlo en el README
        return Double.parseDouble(System.getenv().getOrDefault("TASA_DEFECTO", "4000"));
    }

    /** Carpeta donde se guardan los JSON (variable DATA_DIR). */
    public static String dataDir() {
        return System.getenv().getOrDefault("DATA_DIR", "data");
    }

    /** Puerto HTTP (variable PORT). Los PaaS suelen asignarlo. */
    public static int puerto() {
        return Integer.parseInt(System.getenv().getOrDefault("PORT", "4567"));
    }
}
