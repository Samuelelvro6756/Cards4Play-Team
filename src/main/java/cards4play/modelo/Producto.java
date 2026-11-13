package cards4play.modelo;

public abstract class Producto {

    private String id;
    private String nombre;
    private double precioUsd;
    private double cop;

    protected Producto(String id, String nombre, double precioUsd) {
        this.id = id;
        this.nombre = nombre;
        this.precioUsd = precioUsd;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioUsd() { return precioUsd; }
    public double getCop() { return cop; }

    /** Recalcula el precio en COP con la tasa recibida. */
    public void actualizarCop(double tasa) {
        this.cop = precioUsd * tasa;
    }
}
