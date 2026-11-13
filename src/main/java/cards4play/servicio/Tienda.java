package cards4play.servicio;

import cards4play.modelo.Cliente;
import cards4play.modelo.Compra;
import cards4play.modelo.Producto;

import java.util.List;

/** Coordina el sistema: contiene el inventario y usa la consulta a la API para los precios. */
public class Tienda {

    private Inventario inventario = new Inventario();
    private ConsultaAPI consultaAPI = new ConsultaAPI();

    public Compra comprar(Cliente c, List<Producto> productos) {
        // TODO(C): crear Compra con un DetalleCompra por producto, marcar boosters como vendidos
        throw new UnsupportedOperationException("TODO");
    }

    public void actualizarPreciosCop() {
        // TODO(C): tasa = consultaAPI.consulta(); recorrer productos y llamar actualizarCop(tasa)
        throw new UnsupportedOperationException("TODO");
    }

    public Inventario getInventario() {
        return inventario;
    }
}
