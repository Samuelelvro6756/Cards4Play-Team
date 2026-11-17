package cards4play.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import cards4play.util.ApiException;

/** Sobre con exactamente 10 cartas. Se vende una vez y se abre una vez. */
public class Booster extends Producto {

    private List<Carta> cartas = new ArrayList<>();   // exactamente 10
    private boolean vendido;
    private boolean abierto;
    private String propietarioId;
    private transient Random random = new Random();
    private static final int CARTAS_POR_BOOSTER = 10;

    public Booster(String id, String nombre, double precioUsd) {
        super(id, nombre, precioUsd);
    }

    /** Arma las 10 cartas sorteando rareza y luego una carta de esa rareza. */
    public void generarCartas(List<Carta> disponibles) {
        
        if (disponibles == null || disponibles.isEmpty()) {
            throw ApiException.conflicto("No hay cartas registradas para generar el booster");
        }
        List<Carta> nuevas = new ArrayList<>();
        for (int i = 0; i < CARTAS_POR_BOOSTER; i++) {
            Rareza sorteada = Rareza.sortear(random);
            nuevas.add(elegirCarta(sorteada, disponibles));
        }
        this.cartas = nuevas;
    }
    /** Elige una carta al azar de la rareza sorteada; si no hay, aplica la regla de respaldo. */
    private Carta elegirCarta(Rareza sorteada, List<Carta> disponibles) {
        List<Carta> candidatas = filtrarPorRareza(disponibles, sorteada);

        // Respaldo 1: bajar a la inferior más cercana que tenga cartas
        Rareza r = sorteada.inferior();
        while (candidatas.isEmpty() && r != null) {
            candidatas = filtrarPorRareza(disponibles, r);
            r = r.inferior();
        }
        // Respaldo 2: si no había nada por debajo, subir con superior()
        r = sorteada.superior();
        while (candidatas.isEmpty() && r != null) {
            candidatas = filtrarPorRareza(disponibles, r);
            r = r.superior();
        }
        return candidatas.get(random.nextInt(candidatas.size()));
    }
    
    private static List<Carta> filtrarPorRareza(List<Carta> cartas, Rareza rareza) {
        List<Carta> resultado = new ArrayList<>();
        for (Carta c : cartas) {
            if (c.getRareza() == rareza) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public boolean estaDisponible() {
        return !vendido;
    }

    public List<Carta> getCartas() { return cartas; }
    public boolean isVendido() { return vendido; }
    public boolean isAbierto() { return abierto; }
    public String getPropietarioId() { return propietarioId; }

    public void restaurar(List<Carta> cartas, boolean vendido, boolean abierto, String propietarioId) {
        this.cartas = cartas;
        this.vendido = vendido;
        this.abierto = abierto;
        this.propietarioId = propietarioId;
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
