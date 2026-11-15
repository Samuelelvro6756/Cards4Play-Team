package cards4play.util;

/**
 * Error de negocio con código HTTP. Se lanza desde cualquier capa (modelo, servicio o rutas)
 * y Main lo convierte en la respuesta {"error": "mensaje"} con ese código.
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int codigo;

    public ApiException(int codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    /** 400: datos mal formados o faltantes. */
    public static ApiException solicitudInvalida(String mensaje) { return new ApiException(400, mensaje); }

    /** 401: sin autenticación o credenciales incorrectas. */
    public static ApiException noAutenticado(String mensaje) { return new ApiException(401, mensaje); }

    /** 403: autenticado pero sin permiso. */
    public static ApiException prohibido(String mensaje) { return new ApiException(403, mensaje); }

    /** 404: el recurso no existe. */
    public static ApiException noEncontrado(String mensaje) { return new ApiException(404, mensaje); }

    /** 409: conflicto (ya vendido, ya abierto, cupo lleno, duplicado...). */
    public static ApiException conflicto(String mensaje) { return new ApiException(409, mensaje); }
}
