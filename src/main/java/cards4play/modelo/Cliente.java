package cards4play.modelo;

import cards4play.Main;
import java.util.List;

public class Cliente extends Usuario {

    public Cliente(String id, String nombreUsuario, String contrasena) {
        super(id, nombreUsuario, contrasena, Rol.CLIENTE);
    }

    public Compra comprar(List<Producto> productos) {
        return Main.tienda.comprar(this, productos);
    }

    public List<Carta> abrirBooster(Booster b) {
        return b.abrir(this);
    }

    public void inscribirseEn(Torneo t) {
        if (t != null) {
            t.inscribir(this);
        }
    }

    public List<Compra> verHistorial() {
        return Main.tienda.listarComprasDe(this.getId());
    }
}
