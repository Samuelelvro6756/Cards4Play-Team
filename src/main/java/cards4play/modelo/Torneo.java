package cards4play.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Torneo {

    private String id;
    private String nombre;
    private LocalDate fecha;
    private int cupoMaximo;
    private List<Cliente> inscritos = new ArrayList<>();

    public Torneo(String id, String nombre, LocalDate fecha, int cupoMaximo) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.cupoMaximo = cupoMaximo;
    }

    public boolean hayCupo() {
        return inscritos.size() < cupoMaximo;
    }

    public boolean estaInscrito(Cliente c) {
        // TODO(C)
        throw new UnsupportedOperationException("TODO");
    }

    public void inscribir(Cliente c) {
        // TODO(C): sin pasarse del cupo (409) y sin inscribir dos veces (409)
        throw new UnsupportedOperationException("TODO");
    }
}
