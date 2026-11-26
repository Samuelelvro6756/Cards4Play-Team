package cards4play.modelo;

import cards4play.Main;

public class Administrador extends Usuario {

    public Administrador(String id, String nombreUsuario, String contrasena) {
        super(id, nombreUsuario, contrasena, Rol.ADMIN);
    }

    /** Registra un cliente nuevo. Valida los datos (400) y el usuario duplicado (409), y lo guarda en usuarios.json. */
    public Cliente registrarCliente(String usuario, String clave) {
        return Main.usuarios.registrarCliente(usuario, clave);
    }

    /** Creación unificada: sirve para cartas, boosters, sellados y accesorios. */
    public void crearProducto(Producto p) {
        Main.tienda.getInventario().agregarProducto(p);
    }

    /** Crea un torneo nuevo y lo persiste en torneos.json. */
    public Torneo crearTorneo(Torneo t) {
        return Main.tienda.crearTorneo(t);
    }
}
