package cards4play.servicio;

import cards4play.modelo.Booster;
import cards4play.modelo.Cliente;
import cards4play.modelo.Compra;
import cards4play.modelo.DetalleCompra;
import cards4play.modelo.Producto;
import cards4play.modelo.Torneo;
import cards4play.repositorio.RepositorioJson;
import cards4play.util.ApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Tienda {

    private final Inventario inventario = new Inventario();
    private final ConsultaAPI consultaAPI = new ConsultaAPI();
    private final RepositorioJson<Torneo> repoTorneos = new RepositorioJson<>("torneos.json", Torneo.class);
    private final RepositorioJson<Compra> repoCompras = new RepositorioJson<>("compras.json", Compra.class);
    private final List<Torneo> torneos = repoTorneos.leerTodos();
    private final List<Compra> compras = repoCompras.leerTodos();

    public synchronized Compra comprar(Cliente c, List<Producto> productos) {
        if (c == null || productos == null || productos.isEmpty()) {
            throw ApiException.solicitudInvalida("El cliente y la lista de productos no pueden estar vacíos.");
        }

        // 1) Validar todo antes de modificar nada
        Set<String> boosters = new HashSet<>();
        for (Producto p : productos) {
            if (p instanceof Booster) {
                Booster b = (Booster) p;
                if (!boosters.add(b.getId())) {
                    throw ApiException.conflicto("Booster repetido en la compra: " + b.getNombre());
                }
                if (b.isVendido()) {
                    throw ApiException.conflicto("El booster " + b.getNombre() + " ya fue vendido");
                }
            }
        }

        // 2) Tasa vigente y su fuente real
        double tasa = actualizarPreciosCop();
        Compra compra = new Compra(UUID.randomUUID().toString().substring(0, 8),
                LocalDateTime.now(), c.getId(), tasa, consultaAPI.getFuenteTasa());

        // 3) Aplicar y guardar
        for (Producto p : productos) {
            if (p instanceof Booster) {
                ((Booster) p).marcarVendido(c);
            }
            compra.agregarDetalle(new DetalleCompra(p, 1, p.getCop()));
        }
        compra.calcularTotal();
        compras.add(compra);
        inventario.guardar();
        repoCompras.guardarTodos(compras);
        return compra;
    }

    public synchronized List<Compra> listarCompras() {
        return new ArrayList<>(compras);
    }

    public synchronized List<Compra> listarComprasDe(String clienteId) {
        List<Compra> resultado = new ArrayList<>();
        for (Compra c : compras) {
            if (c.getClienteId() != null && c.getClienteId().equals(clienteId)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public synchronized Compra buscarCompra(String id) {
        for (Compra c : compras) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /** Actualiza el cop de todos los productos y devuelve la tasa usada. */
    public double actualizarPreciosCop() {
        double tasa = consultaAPI.consulta();
        for (Producto p : inventario.listarProductos()) {
            p.actualizarCop(tasa);
        }
        return tasa;
    }

    public Inventario getInventario() { return inventario; }
    public ConsultaAPI getConsultaAPI() { return consultaAPI; }

    public synchronized Torneo crearTorneo(Torneo torneo) {
        if (torneo == null) {
            throw ApiException.solicitudInvalida("El torneo no puede ser nulo.");
        }
        torneos.add(torneo);
        repoTorneos.guardarTodos(torneos);
        return torneo;
    }

    public synchronized List<Torneo> listarTorneos() {
        return new ArrayList<>(torneos);
    }

    public synchronized Torneo buscarTorneo(String id) {
        for (Torneo t : torneos) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    public synchronized void inscribirEnTorneo(String idTorneo, Cliente cliente) {
        for (Torneo t : torneos) {
            if (t.getId().equals(idTorneo)) {
                t.inscribir(cliente);
                repoTorneos.guardarTodos(torneos);
                return;
            }
        }
        throw ApiException.noEncontrado("Torneo no encontrado: " + idTorneo);
    }
}
