package cards4play.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Recibo con una o más líneas de detalle. */
public class Compra {

    private String id;
    private LocalDateTime fecha;
    private List<DetalleCompra> detalles = new ArrayList<>();
    private double totalCop;
    private String fuenteTasa;   // "API" o "DEFECTO"

    public Compra(String id, LocalDateTime fecha, String fuenteTasa) {
        this.id = id;
        this.fecha = fecha;
        this.fuenteTasa = fuenteTasa;
    }

    public void agregarDetalle(DetalleCompra d) {
        detalles.add(d);
    }

    public double calcularTotal() {
        // TODO(C): sumar los subtotales y guardar en totalCop
        throw new UnsupportedOperationException("TODO");
    }
}
