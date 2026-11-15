# Guía para B y C: cómo trabajar sobre la base compartida

A dejó listo lo que necesitan para empezar: errores con código HTTP, autenticación, JSON y la forma de registrar rutas.
Esta guía dice cómo usarlo. Todo lo que está aquí ya existe en el código.

## 1. Primeros pasos

1. `git pull` de `main` (para tener la base de A).
2. Crear una rama por funcionalidad, no por persona. Ejemplos: `feature/inventario-productos`, `feature/compras`.
3. Abrir el proyecto en NetBeans, **Clean and Build** y ejecutar `cards4play.Main`.
4. Probar en Postman con **Authorization → Basic Auth**:
   - Administrador por defecto: usuario `admin`, clave `admin123` (se cambian con las variables `ADMIN_USUARIO` y `ADMIN_CLAVE`).
   - Un cliente se crea con `POST /clientes` (como admin) y el cuerpo `{"usuario": "ana", "clave": "1234"}`.

## 2. Cómo se escribe una ruta

Cada uno trabaja en **su** clase de rutas (así casi no hay conflictos en Git):

| Quién | Clase | Ya está llamada desde `Main` |
|---|---|---|
| B | `api/RutasProductos` | sí |
| C | `api/RutasCompras` y `api/RutasTorneos` | sí |

Ejemplo (dentro de `registrar()`):

```java
post("/productos", (req, res) -> {
    Seguridad.exigirAdmin(req);                       // 403 si no es admin
    NuevoProducto datos = Cuerpo.leer(req, NuevoProducto.class);   // 400 si el JSON está vacío o mal formado
    // ... validar y crear ...
    res.status(201);                                  // código de éxito
    return Json.aJson(resultado);                     // el texto JSON de la respuesta
});
```

Imports que necesitan: `static spark.Spark.get`, `static spark.Spark.post`, y las clases de `cards4play.api` y `cards4play.util`.
Los parámetros de la dirección (por ejemplo `/productos/:id`) se leen con `req.params(":id")`.

## 3. Herramientas ya disponibles

| Necesito... | Uso |
|---|---|
| Saber quién llama | `Seguridad.usuarioActual(req)` |
| Exigir administrador | `Seguridad.exigirAdmin(req)` (403 si no lo es) |
| Exigir cliente | `Seguridad.exigirCliente(req)` (403 si no lo es) |
| Leer el cuerpo JSON | `Cuerpo.leer(req, MiClase.class)` |
| Responder con JSON | `Json.aJson(objeto)` |
| Devolver un error | `throw ApiException.noEncontrado("...")` |
| Buscar un cliente por id | `Main.usuarios.buscarClientePorId(id)` |
| Usar la tienda y el inventario | `Main.tienda` y `Main.tienda.getInventario()` |

Errores disponibles en `ApiException`, todos con el formato `{"error": "mensaje"}`:

| Método | Código |
|---|---|
| `solicitudInvalida` | 400 |
| `noAutenticado` | 401 |
| `prohibido` | 403 |
| `noEncontrado` | 404 |
| `conflicto` | 409 |

Pueden lanzarlos desde cualquier clase (modelo, servicio o ruta). `Main` los convierte en la respuesta.

## 4. Persistencia en JSON

- Usen `RepositorioJson` con un archivo propio: `productos.json` (B), `compras.json` y `torneos.json` (C).
- **No guarden directamente clases con herencia** (`Producto` y sus hijas). Hagan como en `servicio/Usuarios.java`: una clase `Registro` plana, con un campo `tipo` (por ejemplo `"CARTA"`, `"BOOSTER"`), y conviértanla a la clase real al leer.
- En las listas anidadas guarden **ids**, no objetos completos (un torneo guarda los ids de sus inscritos; un booster guarda los ids de sus cartas).
- Las fechas (`LocalDate`, `LocalDateTime`) ya funcionan con `Json`; no creen otro `Gson`.

## 5. Reglas del equipo

- Cada ruta devuelve JSON y los errores siempre con el formato `{"error": "..."}`.
- Todas las rutas (menos `/salud`) ya piden autenticación; no hay que hacer nada para eso.
- Solo toquen los archivos de su área. Si necesitan cambiar algo de otro (por ejemplo `Main` o `pom.xml`), avisen primero.
- Todo entra a `main` por Pull Request con revisión cruzada.
