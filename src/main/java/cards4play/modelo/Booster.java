package cards4play.modelo;

import cards4play.util.ApiException;
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
    private static final int CARTAS_POR_BOOSTER = 10;

    public Booster(String id, String nombre, double precioUsd) {
        super(id, nombre, precioUsd);
    }

    /**
     * Arma las 10 cartas sorteando rareza y luego una carta de esa rareza.
     * Incluye la bonificación: garantiza al menos una carta R o superior si existen
     * cartas de rareza R, SR o UR en el inventario, sin alterar la tabla de probabilidades.
     */
    public void generarCartas(List<Carta> disponibles) {
        if (disponibles == null || disponibles.isEmpty()) {
            throw ApiException.conflicto("No hay cartas registradas para generar el booster");
        }
        List<Carta> nuevas = new ArrayList<>();
        for (int i = 0; i < CARTAS_POR_BOOSTER; i++) {
            Rareza sorteada = Rareza.sortear(random);
            nuevas.add(elegirCarta(sorteada, disponibles));
        }

        // Bonificación: garantizar al menos una carta R o superior (si existen en el catálogo)
        boolean tieneRaraOSuperior = false;
        for (Carta c : nuevas) {
            if (c.getRareza().esRaraOSuperior()) {
                tieneRaraOSuperior = true;
                break;
            }
        }
        if (!tieneRaraOSuperior) {
            List<Carta> rarasDisponibles = new ArrayList<>();
            for (Carta c : disponibles) {
                if (c.getRareza().esRaraOSuperior()) {
                    rarasDisponibles.add(c);
                }
            }
            if (!rarasDisponibles.isEmpty()) {
                Rareza sorteada = Rareza.sortearRaraOSuperior(random);
                Carta garantizada = elegirCarta(sorteada, rarasDisponibles);
                nuevas.set(random.nextInt(CARTAS_POR_BOOSTER), garantizada);
            }
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
        if (vendido) {
            throw ApiException.conflicto("El booster ya fue vendido");
        }
        this.vendido = true;
        this.propietarioId = c.getId();
    }

    public List<Carta> abrir(Cliente c) {
        if (!vendido || propietarioId == null) {
            throw ApiException.prohibido("Este booster aún no ha sido comprado");
        }
        if (!propietarioId.equals(c.getId())) {
            throw ApiException.prohibido("Este booster pertenece a otro cliente");
        }
        if (abierto) {
            throw ApiException.conflicto("El booster ya fue abierto");
        }
        this.abierto = true;
        return new ArrayList<>(cartas);
    }
}
