package cards4play.servicio;

import cards4play.modelo.Booster;
import cards4play.modelo.Carta;
import cards4play.modelo.Producto;
import cards4play.modelo.Rareza;

import java.util.ArrayList;
import java.util.List;

/** Guarda y administra los productos de la tienda. */
public class Inventario {

    private List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto p) {
        // TODO(B): validar y persistir en productos.json
        throw new UnsupportedOperationException("TODO");
    }

    public Producto buscarPorId(String id) {
        // TODO(B): devolver null (o error 404) si no existe
        throw new UnsupportedOperationException("TODO");
    }

    public List<Producto> listarProductos() {
        return productos;
    }

    public List<Carta> cartasPorRareza(Rareza r) {
        // TODO(B): lo usa Booster.generarCartas para la regla de respaldo
        throw new UnsupportedOperationException("TODO");
    }

    public List<Booster> listarBoostersDisponibles() {
        // TODO(B): solo boosters no vendidos, sin mostrar sus cartas
        throw new UnsupportedOperationException("TODO");
    }
}
