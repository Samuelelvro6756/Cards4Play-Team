package cards4play.servicio;

import cards4play.modelo.Cliente;
import cards4play.modelo.Compra;
import cards4play.modelo.DetalleCompra;
import cards4play.modelo.Producto;
import cards4play.modelo.Booster;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Tienda {

    private Inventario inventario = new Inventario();
    private ConsultaAPI consultaAPI = new ConsultaAPI();

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

    public void actualizarPreciosCop() {
        double tasa = consultaAPI.consulta();
        for (Producto p : inventario.listarProductos()) {
            p.actualizarCop(tasa);
        }
    }

    public Inventario getInventario() {
        return inventario;
    }

    public ConsultaAPI getConsultaAPI() {
        return consultaAPI;
    }
}