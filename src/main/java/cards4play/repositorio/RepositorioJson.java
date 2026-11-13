package cards4play.repositorio;

import cards4play.util.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Base para leer y escribir una lista de objetos en un archivo JSON dentro de DATA_DIR.
 * Uso: new RepositorioJson<>("productos.json", Producto.class)
 * NOTA: las jerarquías (Producto, Usuario) necesitarán un adaptador de Gson para
 * saber a qué subclase corresponde cada objeto (TODO del equipo).
 */
public class RepositorioJson<T> {

    private final Path archivo;
    private final Type tipoLista;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RepositorioJson(String nombreArchivo, Class<T> clase) {
        this.archivo = Paths.get(Config.dataDir(), nombreArchivo);
        this.tipoLista = TypeToken.getParameterized(List.class, clase).getType();
    }

    public List<T> leerTodos() {
        try {
            if (!Files.exists(archivo)) {
                return new ArrayList<>();
            }
            String json = Files.readString(archivo, StandardCharsets.UTF_8);
            List<T> lista = gson.fromJson(json, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer " + archivo, e);
        }
    }

    public void guardarTodos(List<T> elementos) {
        try {
            Files.createDirectories(archivo.getParent());
            Files.writeString(archivo, gson.toJson(elementos, tipoLista), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo escribir " + archivo, e);
        }
    }
}
