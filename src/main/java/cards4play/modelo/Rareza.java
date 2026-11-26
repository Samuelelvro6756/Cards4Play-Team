package cards4play.modelo;

import java.util.Random;

/** Rarezas de las cartas. Las probabilidades viven SOLO aquí y suman 100 %. */
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

    /** Indica si la rareza es R o superior (R, SR, UR). */
    public boolean esRaraOSuperior() {
        return this.ordinal() >= R.ordinal();
    }

    /** Sortea una rareza según la tabla de probabilidades. */
    public static Rareza sortear(Random r) {
        int numero = r.nextInt(100);
        int acumulado = 0;
        for (Rareza rareza : values()) {
            acumulado += rareza.probabilidad;
            if (numero < acumulado) {
                return rareza;
            }
        }
        return N;
    }

    /**
     * Sortea una rareza dentro de R, SR y UR respetando exactamente sus pesos relativos
     * definidos en este mismo enum (para la bonificación de garantía de al menos una R+).
     */
    public static Rareza sortearRaraOSuperior(Random r) {
        int totalPeso = 0;
        for (Rareza rareza : values()) {
            if (rareza.esRaraOSuperior()) {
                totalPeso += rareza.probabilidad;
            }
        }
        int numero = r.nextInt(totalPeso);
        int acumulado = 0;
        for (Rareza rareza : values()) {
            if (rareza.esRaraOSuperior()) {
                acumulado += rareza.probabilidad;
                if (numero < acumulado) {
                    return rareza;
                }
            }
        }
        return R;
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
