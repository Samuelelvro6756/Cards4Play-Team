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

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecioUsd(double precioUsd) { this.precioUsd = precioUsd; }

    /** Recalcula el precio en COP con la tasa recibida (redondeado a 2 decimales). */
    public void actualizarCop(double tasa) {
        this.cop = Math.round(precioUsd * tasa * 100.0) / 100.0;
    }
}
