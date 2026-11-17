package cards4play.modelo;
import cards4play.Main;

public class Administrador extends Usuario {

    public Administrador(String id, String nombreUsuario, String contrasena) {
        super(id, nombreUsuario, contrasena, Rol.ADMIN);
    }

    public Cliente registrarCliente(String usuario, String clave) {
        // TODO(A): validar usuario duplicado (409) y persistir
        throw new UnsupportedOperationException("TODO");
    }

    /** Creación unificada: sirve para cartas, boosters, sellados y accesorios. */
    public void crearProducto(Producto p) {
        Main.tienda.getInventario().agregarProducto(p);
    }

    public void crearTorneo(Torneo t) {
        // TODO(C): agregar a la lista de torneos
        throw new UnsupportedOperationException("TODO");
    }
}
