package cards4play.modelo;

/** Ítem del recibo de una compra. */
public class DetalleCompra {

    private String productoId;
    private String nombre;
    private int cantidad;
    private double precioUnitarioCop;

    public DetalleCompra(Producto producto, int cantidad, double precioUnitarioCop) {
        this.productoId = producto.getId();
        this.nombre = producto.getNombre();
        this.cantidad = cantidad;
        this.precioUnitarioCop = precioUnitarioCop;
    }

    public double subtotal() {
        return cantidad * precioUnitarioCop;
    }
    
    public String getProductoId() { return productoId; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitarioCop() { return precioUnitarioCop; }
}
