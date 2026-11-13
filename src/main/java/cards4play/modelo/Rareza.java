package cards4play.modelo;

import java.util.Random;

/** Rarezas de las cartas. Las probabilidades viven SOLO aquí. */
public enum Rareza {
    N(50),
    UC(25),
    R(15),
    SR(7),
    UR(3);

    private final int probabilidad;

    Rareza(int probabilidad) {
        this.probabilidad = probabilidad;
    }

    public int getProbabilidad() {
        return probabilidad;
    }

    /** Sortea una rareza según las probabilidades. */
    public static Rareza sortear(Random r) {
        // TODO(B): sorteo acumulado con r.nextInt(100)
        throw new UnsupportedOperationException("TODO");
    }

    /** Rareza inmediatamente inferior, o null si es la más baja. */
    public Rareza inferior() {
        return ordinal() > 0 ? values()[ordinal() - 1] : null;
    }

    /** Rareza inmediatamente superior, o null si es la más alta. */
    public Rareza superior() {
        return ordinal() < values().length - 1 ? values()[ordinal() + 1] : null;
    }
}
