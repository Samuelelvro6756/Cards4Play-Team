# Cards4Play

Backend (API REST en Java) de una tienda de cartas coleccionables con torneos. Proyecto final de Desarrollo de Software.

## Integrantes

- Samuel Duran
- Maia Saavedra
- Miguel Rodriguez

## Tecnología

Java 17, Maven, Spark Java (API REST), Gson (JSON) y archivos JSON como persistencia. Se prueba con Postman y se empaqueta con Docker.

## Cómo ejecutarlo

```bash
mvn package
java -jar target/cards4play.jar
```

En NetBeans: **Clean and Build** y ejecutar la clase `cards4play.Main`.

Prueba rápida: `GET http://localhost:4567/salud` debe responder `{"estado":"ok"}`.

## Autenticación

Toda la API (menos `/salud`) usa **HTTP Basic**: usuario y clave en cada petición.

El administrador se crea solo al arrancar si no existe:

| Usuario | Clave |
|---|---|
| `admin` | `admin123` |

Se cambian con las variables `ADMIN_USUARIO` y `ADMIN_CLAVE`. El administrador registra a los clientes con `POST /clientes`. Las respuestas nunca incluyen la contraseña.

## Variables de entorno

| Variable | Qué hace | Valor por defecto |
|---|---|---|
| `PORT` | Puerto HTTP | `4567` |
| `DATA_DIR` | Carpeta de los archivos JSON | `data` |
| `TASA_DEFECTO` | Tasa USD→COP si la API externa falla | _(provisional: `4000`; falta definir el valor final)_ |
| `ADMIN_USUARIO` | Usuario del administrador por defecto | `admin` |
| `ADMIN_CLAVE` | Clave del administrador por defecto | `admin123` |

## Endpoints

Lista completa, con códigos HTTP y permisos, en [docs/CONTRATO_API.md](docs/CONTRATO_API.md).
Todos los errores tienen el mismo formato: `{"error": "mensaje"}`.

## Docker

Construir la imagen (desde la carpeta del proyecto, donde está el `Dockerfile`):

```bash
docker build -t cards4play .
```

Ejecutar el contenedor con volumen para los datos y variable de entorno:

```bash
docker run -d --name cards4play -p 4567:4567 -v cards4play-data:/app/data -e TASA_DEFECTO=4000 cards4play
```

- `-p 4567:4567` publica el puerto de la API.
- `-v cards4play-data:/app/data` guarda los JSON en un volumen, así los datos sobreviven si se borra el contenedor.
- `-e TASA_DEFECTO=4000` define la tasa que se usa si la API de cambio no responde (cambiar por el valor definitivo).

Comprobar la persistencia:

1. Ejecutar el contenedor y crear datos (por ejemplo, un cliente con `POST /clientes`).
2. Borrar el contenedor: `docker rm -f cards4play`.
3. Volver a ejecutar el mismo `docker run` (con el mismo volumen).
4. Consultar `GET /clientes`: los datos siguen ahí.

## Colecciones de Postman

En la carpeta [`postman/`](postman/):

- `Cards4Play.postman_environment.json`: variables compartidas (`baseUrl`, `adminUsuario`, `adminClave`).
- `Cards4Play-Auth-Clientes.postman_collection.json`: autenticación y clientes, con casos de éxito y de error.

Cada integrante agrega la colección de su área. Importar el entorno y las colecciones en Postman y ejecutarlas con **Run collection** (con el servidor encendido).

## API externa

_(Pendiente: cuál se usa para la tasa USD→COP.)_

## URL pública (PaaS)

_(Pendiente)_

## Flujo de Git

`main` siempre estable. Una rama por funcionalidad (no una por persona) y Pull Request con revisión cruzada: A revisa a B, B revisa a C y C revisa a A.

_(Pendiente: documentar aquí el conflicto de merge resuelto.)_

## Diagrama de clases

Ver [Diagrama de clases.md](Diagrama%20de%20clases.md) (también en imagen: `docs/diagrama_clases_cards4play_v3.png`).
