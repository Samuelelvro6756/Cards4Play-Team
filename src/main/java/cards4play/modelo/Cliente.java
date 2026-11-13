package cards4play.modelo;

import cards4play.servicio.Tienda;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private List<Compra> historial = new ArrayList<>();

    public Cliente(String id, String nombreUsuario, String contrasena) {
        super(id, nombreUsuario, contrasena, Rol.CLIENTE);
    }

    public Compra comprar(Tienda t, List<Producto> productos) {
        // TODO(C): delegar en Tienda.comprar y agregar la compra al historial
        throw new UnsupportedOperationException("TODO");
    }

    public List<Carta> abrirBooster(Booster b) {
        // TODO(B): delegar en Booster.abrir(this)
        throw new UnsupportedOperationException("TODO");
    }

    public void inscribirseEn(Torneo t) {
        // TODO(C): delegar en Torneo.inscribir(this)
        throw new UnsupportedOperationException("TODO");
    }

    public List<Compra> verHistorial() {
        return historial;
    }
}
