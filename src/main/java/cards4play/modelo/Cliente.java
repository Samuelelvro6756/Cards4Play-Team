package cards4play.modelo;
import java.util.UUID;
import java.time.LocalDateTime;

import cards4play.servicio.Tienda;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private List<Compra> historial = new ArrayList<>();

    public Cliente(String id, String nombreUsuario, String contrasena) {
        super(id, nombreUsuario, contrasena, Rol.CLIENTE);
    }

public Compra comprar(Cliente c, List<Producto> productos) {
        if (c == null || productos == null || productos.isEmpty()) {
            throw new IllegalArgumentException("El cliente y la lista de productos no pueden estar vacíos.");
        }

        String idCompra = UUID.randomUUID().toString().substring(0, 8);
        Compra compra = new Compra(idCompra, LocalDateTime.now(), "API");

for (Producto p : productos) {
            if (p instanceof Booster) {
                ((Booster) p).marcarVendido(c);
            }
            DetalleCompra detalle = new DetalleCompra(p, 1, p.getCop());
            compra.agregarDetalle(detalle);
        }

        compra.calcularTotal();
        return compra;
    }
    public List<Carta> abrirBooster(Booster b) {
        return  b.abrir(this);
    }

    public void inscribirseEn(Torneo t) {
        if (t != null) {
            t.inscribir(this);
        }
    }
    public List<Compra> verHistorial() {
        return historial;
    }
}
