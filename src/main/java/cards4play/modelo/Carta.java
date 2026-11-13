package cards4play.modelo;

public class Carta extends Producto {

    private Rareza rareza;

    public Carta(String id, String nombre, double precioUsd, Rareza rareza) {
        super(id, nombre, precioUsd);
        this.rareza = rareza;
    }

    public Rareza getRareza() { return rareza; }
}
