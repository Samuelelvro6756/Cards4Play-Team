package cards4play.servicio;

import cards4play.modelo.Administrador;
import cards4play.modelo.Cliente;
import cards4play.modelo.Rol;
import cards4play.modelo.Usuario;
import cards4play.repositorio.RepositorioJson;
import cards4play.util.ApiException;
import cards4play.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Guarda y busca usuarios (administrador y clientes) en usuarios.json. */
public class Usuarios {

    /** Forma plana en que se guarda cada usuario en el JSON (sin jerarquía, para que Gson no se confunda). */
    static class Registro {
        String id;
        String nombreUsuario;
        String contrasena;
        String rol;
    }

    private final RepositorioJson<Registro> repositorio = new RepositorioJson<>("usuarios.json", Registro.class);
    private final List<Usuario> usuarios = new ArrayList<>();

    public Usuarios() {
        for (Registro r : repositorio.leerTodos()) {
            usuarios.add(aUsuario(r));
        }
        asegurarAdminPorDefecto();
    }

    /** Devuelve el usuario si el nombre y la clave son correctos; si no, null. */
    public synchronized Usuario autenticar(String usuario, String clave) {
        for (Usuario u : usuarios) {
            if (u.autenticar(usuario, clave)) {
                return u;
            }
        }
        return null;
    }

    public synchronized Cliente registrarCliente(String usuario, String clave) {
        if (usuario == null || usuario.isBlank() || clave == null || clave.isBlank()) {
            throw ApiException.solicitudInvalida("Se requieren 'usuario' y 'clave'");
        }
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(usuario)) {
                throw ApiException.conflicto("Ya existe un usuario con ese nombre");
            }
        }
        Cliente cliente = new Cliente(UUID.randomUUID().toString(), usuario, clave);
        usuarios.add(cliente);
        guardar();
        return cliente;
    }

    public synchronized List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Cliente) {
                clientes.add((Cliente) u);
            }
        }
        return clientes;
    }

    /** Busca un cliente por id (lo usarán C para compras y torneos). Devuelve null si no existe. */
    public synchronized Cliente buscarClientePorId(String id) {
        for (Usuario u : usuarios) {
            if (u instanceof Cliente && u.getId().equals(id)) {
                return (Cliente) u;
            }
        }
        return null;
    }

    private void asegurarAdminPorDefecto() {
        for (Usuario u : usuarios) {
            if (u.getRol() == Rol.ADMIN) {
                return;
            }
        }
        usuarios.add(new Administrador(UUID.randomUUID().toString(), Config.adminUsuario(), Config.adminClave()));
        guardar();
    }

    private void guardar() {
        List<Registro> registros = new ArrayList<>();
        for (Usuario u : usuarios) {
            Registro r = new Registro();
            r.id = u.getId();
            r.nombreUsuario = u.getNombreUsuario();
            r.contrasena = u.getContrasena();
            r.rol = u.getRol().name();
            registros.add(r);
        }
        repositorio.guardarTodos(registros);
    }

    private static Usuario aUsuario(Registro r) {
        if (Rol.valueOf(r.rol) == Rol.ADMIN) {
            return new Administrador(r.id, r.nombreUsuario, r.contrasena);
        }
        return new Cliente(r.id, r.nombreUsuario, r.contrasena);
    }
}
