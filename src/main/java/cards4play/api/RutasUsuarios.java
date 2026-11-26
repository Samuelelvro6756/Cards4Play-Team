package cards4play.api;

import cards4play.Main;
import cards4play.modelo.Administrador;
import cards4play.modelo.Cliente;
import cards4play.modelo.Usuario;
import cards4play.util.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

/** Rutas de usuarios: quién soy y administración de clientes. */
public final class RutasUsuarios {

    private RutasUsuarios() {}

    /** Cuerpo esperado en POST /clientes. */
    private static class NuevoCliente {
        String usuario;
        String clave;
    }

    public static void registrar() {
        // Sirve para probar la autenticación desde Postman
        get("/yo", (req, res) -> Json.aJson(vista(Seguridad.usuarioActual(req))));

        post("/clientes", (req, res) -> {
            Administrador admin = Seguridad.exigirAdmin(req);
            NuevoCliente nuevo = Cuerpo.leer(req, NuevoCliente.class);
            Cliente cliente = admin.registrarCliente(nuevo.usuario, nuevo.clave);
            res.status(201);
            return Json.aJson(vista(cliente));
        });

        get("/clientes", (req, res) -> {
            Seguridad.exigirAdmin(req);
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Cliente c : Main.usuarios.listarClientes()) {
                lista.add(vista(c));
            }
            return Json.aJson(lista);
        });
    }

    /** Lo que se muestra de un usuario: nunca la contraseña. */
    private static Map<String, Object> vista(Usuario usuario) {
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.put("id", usuario.getId());
        mapa.put("usuario", usuario.getNombreUsuario());
        mapa.put("rol", usuario.getRol().name());
        return mapa;
    }
}
