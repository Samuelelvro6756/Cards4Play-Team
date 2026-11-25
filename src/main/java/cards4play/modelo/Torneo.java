package cards4play.modelo;
import cards4play.util.ApiException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Torneo {

    private String id;
    private String nombre;
    private LocalDate fecha;
    private int cupoMaximo;
    private List<String> inscritos = new ArrayList<>();

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
        return c != null && inscritos.contains(c.getId());
    }

    public void inscribir(Cliente c) {
         if (c == null) {
        throw ApiException.solicitudInvalida("Falta el cliente");
    }
    if (!hayCupo()) {
        throw ApiException.conflicto("El torneo ya alcanzó el cupo máximo");
    }
    if (estaInscrito(c)) {
        throw ApiException.conflicto("El cliente ya está inscrito en este torneo");
    }
    inscritos.add(c.getId());
    }
    
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public List<String> getInscritos() {
        return inscritos;
    }
}
