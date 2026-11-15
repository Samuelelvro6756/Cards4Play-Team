package cards4play.api;

import cards4play.modelo.Administrador;
import cards4play.modelo.Cliente;
import cards4play.modelo.Usuario;
import cards4play.servicio.Usuarios;
import cards4play.util.ApiException;
import spark.Request;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Autenticación HTTP Basic y permisos por rol.
 * Main ejecuta autenticar() antes de cada petición (menos /salud). En las rutas basta con
 * llamar a usuarioActual(), exigirAdmin() o exigirCliente().
 */
public final class Seguridad {

    public static final String ATRIBUTO_USUARIO = "usuario";

    private Seguridad() {}

    /** Lee el encabezado Authorization. 401 si falta, está mal formado o las credenciales no coinciden. */
    public static Usuario autenticar(Request req, Usuarios usuarios) {
        String encabezado = req.headers("Authorization");
        if (encabezado == null || !encabezado.startsWith("Basic ")) {
            throw ApiException.noAutenticado("Falta la autenticación (HTTP Basic)");
        }
        String credenciales;
        try {
            byte[] bytes = Base64.getDecoder().decode(encabezado.substring(6).trim());
            credenciales = new String(bytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw ApiException.noAutenticado("Credenciales mal formadas");
        }
        int separador = credenciales.indexOf(':');
        if (separador < 0) {
            throw ApiException.noAutenticado("Credenciales mal formadas");
        }
        Usuario usuario = usuarios.autenticar(credenciales.substring(0, separador), credenciales.substring(separador + 1));
        if (usuario == null) {
            throw ApiException.noAutenticado("Usuario o contraseña incorrectos");
        }
        return usuario;
    }

    /** El usuario autenticado de esta petición (401 si no hay). */
    public static Usuario usuarioActual(Request req) {
        Usuario usuario = req.attribute(ATRIBUTO_USUARIO);
        if (usuario == null) {
            throw ApiException.noAutenticado("Falta la autenticación (HTTP Basic)");
        }
        return usuario;
    }

    /** 403 si el usuario autenticado no es administrador. */
    public static Administrador exigirAdmin(Request req) {
        Usuario usuario = usuarioActual(req);
        if (!(usuario instanceof Administrador)) {
            throw ApiException.prohibido("Solo el administrador puede hacer esto");
        }
        return (Administrador) usuario;
    }

    /** 403 si el usuario autenticado no es un cliente. */
    public static Cliente exigirCliente(Request req) {
        Usuario usuario = usuarioActual(req);
        if (!(usuario instanceof Cliente)) {
            throw ApiException.prohibido("Solo un cliente puede hacer esto");
        }
        return (Cliente) usuario;
    }
}
