package cards4play.servicio;

import cards4play.modelo.Cliente;
import cards4play.modelo.Compra;
import cards4play.modelo.Producto;
import java.util.List;


public class Tienda {

    private Inventario inventario = new Inventario();
    private ConsultaAPI consultaAPI = new ConsultaAPI();

    public Compra comprar(Cliente c, List<Producto> productos) {
        throw new UnsupportedOperationException("TODO");
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