package cards4play.servicio;

import cards4play.modelo.Accesorio;
import cards4play.modelo.Booster;
import cards4play.modelo.Carta;
import cards4play.modelo.Producto;
import cards4play.modelo.ProductoSellado;
import cards4play.modelo.Rareza;
import cards4play.repositorio.RepositorioJson;
import cards4play.util.ApiException;

import java.util.ArrayList;
import java.util.List;

/** Guarda y administra los productos de la tienda. */
public class Inventario {

    static class Registro {
        String tipo;               // CARTA, BOOSTER, SELLADO, ACCESORIO
        String id;
        String nombre;
        double precioUsd;
        String rareza;             // solo CARTA
        List<String> cartaIds;     // solo BOOSTER: ids de sus cartas
        boolean vendido;           // solo BOOSTER
        boolean abierto;           // solo BOOSTER
        String propietarioId;      // solo BOOSTER
    }

    private final RepositorioJson<Registro> repositorio = new RepositorioJson<>("Productos.json", Registro.class);
    private List<Producto> productos = new ArrayList<>();

    public  Inventario(){
        List<Registro> registros = repositorio.leerTodos();
        for(Registro r : registros){
            if (!"BOOSTER".equals(r.tipo)) {
                productos.add(aProducto(r));
                
            }
        }
        for (Registro r : registros) {
            if ("BOOSTER".equals(r.tipo)) {
                productos.add(aBooster(r));
            }
        }
    }

    public void validar(Producto p){
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw ApiException.solicitudInvalida("El nombre es obligatorio");
        }
        if (p.getPrecioUsd() <= 0) {
            throw ApiException.solicitudInvalida("El precio debe ser mayor que 0");
        }
        if (p instanceof Carta && ((Carta) p).getRareza() == null) {
            throw ApiException.solicitudInvalida("La carta necesita una rareza");
        }


    }
    public void agregarProducto(Producto p) {
         validar(p);
        if (buscarPorId(p.getId()) != null) {
            throw ApiException.conflicto("Ya existe un producto con ese id");
        }
        productos.add(p);
        guardar();
    }

    public Producto buscarPorId(String id) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(productos);
    }

    public List<Carta> cartasPorRareza(Rareza r) {
        List<Carta> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p instanceof Carta && ((Carta) p).getRareza() == r) {
                resultado.add((Carta) p);
            }
        }
        return resultado;
    }
    
    public synchronized List<Carta> listarCartas() {
        List<Carta> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p instanceof Carta) {
                resultado.add((Carta) p);
            }
        }
        return resultado;
    }

    public List<Booster> listarBoostersDisponibles() {
        List<Booster> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p instanceof Booster && ((Booster) p).estaDisponible()) {
                resultado.add((Booster) p);
            }
        }
        return resultado;
    }

    public synchronized void guardar() {
        List<Registro> registros = new ArrayList<>();
        for (Producto p : productos) {
            registros.add(aRegistro(p));
        }
        repositorio.guardarTodos(registros);
    }

    public static String tipoDe(Producto p) {
        if (p instanceof Carta) return "CARTA";
        if (p instanceof Booster) return "BOOSTER";
        if (p instanceof ProductoSellado) return "SELLADO";
        if (p instanceof Accesorio) return "ACCESORIO";
        throw new IllegalArgumentException("Tipo de producto desconocido");
    }

    private static Registro aRegistro(Producto p) {
        Registro r = new Registro();
        r.tipo = tipoDe(p);
        r.id = p.getId();
        r.nombre = p.getNombre();
        r.precioUsd = p.getPrecioUsd();
        if (p instanceof Carta) {
            r.rareza = ((Carta) p).getRareza().name();
        }
        if (p instanceof Booster) {
            Booster b = (Booster) p;
            r.cartaIds = new ArrayList<>();
            for (Carta c : b.getCartas()) {
                r.cartaIds.add(c.getId());
            }
            r.vendido = b.isVendido();
            r.abierto = b.isAbierto();
            r.propietarioId = b.getPropietarioId();
        }
        return r;
    }

    private static Producto aProducto(Registro r) {
        switch (r.tipo) {
            case "CARTA":
                return new Carta(r.id, r.nombre, r.precioUsd, Rareza.valueOf(r.rareza));
            case "SELLADO":
                return new ProductoSellado(r.id, r.nombre, r.precioUsd);
            case "ACCESORIO":
                return new Accesorio(r.id, r.nombre, r.precioUsd);
            default:
                throw new IllegalStateException("Tipo desconocido en productos.json: " + r.tipo);
        }
    }

    private Booster aBooster(Registro r) {
        List<Carta> cartas = new ArrayList<>();
        if (r.cartaIds != null) {
            for (String idCarta : r.cartaIds) {
                Producto p = buscarPorId(idCarta);
                if (p instanceof Carta) {
                    cartas.add((Carta) p);
                }
            }
        }
        Booster b = new Booster(r.id, r.nombre, r.precioUsd);
        b.restaurar(cartas, r.vendido, r.abierto, r.propietarioId);
        return b;
    }
}
