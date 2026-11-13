package cards4play.modelo;

/** Ítem del recibo de una compra. */
public class DetalleCompra {

    private Producto producto;
    private int cantidad;
    private double precioUnitarioCop;

    public DetalleCompra(Producto producto, int cantidad, double precioUnitarioCop) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitarioCop = precioUnitarioCop;
    }

    public double subtotal() {
        return cantidad * precioUnitarioCop;
    }
}
