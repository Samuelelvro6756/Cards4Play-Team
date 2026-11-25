package cards4play.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private String clienteId;
    private double tasaUsada;
    private String id;
    private LocalDateTime fecha;
    private List<DetalleCompra> detalles = new ArrayList<>();
    private double totalCop;
    private String fuenteTasa; // "API" o "DEFECTO"

    public Compra(String id, LocalDateTime fecha, String fuenteTasa) {
        this.id = id;
        this.fecha = fecha;
        this.fuenteTasa = fuenteTasa;
    }
    
    public Compra(String id, LocalDateTime fecha, String clienteId, double tasaUsada, String fuenteTasa) {
    this.id = id;
    this.fecha = fecha;
    this.clienteId = clienteId;
    this.tasaUsada = tasaUsada;
    this.fuenteTasa = fuenteTasa;
}

    public void agregarDetalle(DetalleCompra d) {
        detalles.add(d);
    }

    public double calcularTotal() {
        double total = 0;
        for (DetalleCompra d : detalles) {
            total += d.subtotal();
        }
        this.totalCop = total;
        return total;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public double getTotalCop() {
        return totalCop;
    }

    public String getFuenteTasa() {
        return fuenteTasa;
    }
    
    public String getClienteId() { return clienteId; }
    
    public double getTasaUsada() { return tasaUsada; }
}