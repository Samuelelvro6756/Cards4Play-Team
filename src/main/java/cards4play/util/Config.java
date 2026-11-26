package cards4play.util;

/** Lee la configuración desde variables de entorno, con valores por defecto. */
public final class Config {

    private Config() {}

    /** Tasa USD->COP que se usa si la API externa no responde (variable TASA_DEFECTO). */
    public static double tasaDefecto() {
        return Double.parseDouble(System.getenv().getOrDefault("TASA_DEFECTO", "4000"));
    }

    /** URL de la API externa de tasa de cambio USD->COP (opcional: API_TASA_URL para pruebas de fallback). */
    public static String urlApiTasa() {
        return System.getenv().getOrDefault("API_TASA_URL", "https://open.er-api.com/v6/latest/USD");
    }

    /** Carpeta donde se guardan los JSON (variable DATA_DIR). */
    public static String dataDir() {
        return System.getenv().getOrDefault("DATA_DIR", "data");
    }

    /** Puerto HTTP (variable PORT). Los PaaS suelen asignarlo. */
    public static int puerto() {
        return Integer.parseInt(System.getenv().getOrDefault("PORT", "4567"));
    }

    /** Usuario del administrador por defecto (variable ADMIN_USUARIO). */
    public static String adminUsuario() {
        return System.getenv().getOrDefault("ADMIN_USUARIO", "admin");
    }

    /** Clave del administrador por defecto (variable ADMIN_CLAVE). */
    public static String adminClave() {
        return System.getenv().getOrDefault("ADMIN_CLAVE", "admin123");
    }
}
