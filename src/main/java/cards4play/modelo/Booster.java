package cards4play.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Sobre con exactamente 10 cartas. Se vende una vez y se abre una vez. */
public class Booster extends Producto {

    private List<Carta> cartas = new ArrayList<>();   // exactamente 10
    private boolean vendido;
    private boolean abierto;
    private String propietarioId;
    private transient Random random = new Random();

    public Booster(String id, String nombre, double precioUsd) {
        super(id, nombre, precioUsd);
    }

    /** Arma las 10 cartas sorteando rareza y luego una carta de esa rareza. */
    public void generarCartas(List<Carta> disponibles) {
        // TODO(B): probabilidades en Rareza, regla de respaldo (inferior, luego superior),
        //          error controlado si no hay ninguna carta registrada
        throw new UnsupportedOperationException("TODO");
    }

    public boolean estaDisponible() {
        return !vendido;
    }

    public void marcarVendido(Cliente c) {
        // TODO(B): validar que no esté vendido (409) y asignar propietarioId
        throw new UnsupportedOperationException("TODO");
    }

    public List<Carta> abrir(Cliente c) {
        // TODO(B): solo el dueño (403), solo una vez (409); el contenido se oculta hasta abrirse
        throw new UnsupportedOperationException("TODO");
    }
}
