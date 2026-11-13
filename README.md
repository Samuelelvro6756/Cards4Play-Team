# Cards4Play

Backend (API REST en Java) de una tienda de cartas coleccionables con torneos. Proyecto final de Desarrollo de Software.

## Integrantes

- Samuel Duran
- Maia Saavedra
- Miguel Rodriguez

## Cómo ejecutarlo

```bash
mvn package
java -jar target/cards4play.jar
```

Prueba rápida: `GET http://localhost:4567/salud`

## Docker

_(Se completa en la Entrega 3: comandos para construir la imagen y correr el contenedor con volumen y variable de entorno.)_

## Variables de entorno

| Variable | Qué hace | Valor por defecto |
|---|---|---|
| `TASA_DEFECTO` | Tasa USD→COP si la API externa falla | _(por definir)_ |
| `DATA_DIR` | Carpeta de los archivos JSON | `data` |
| `PORT` | Puerto HTTP | `4567` |

## Endpoints

Ver [docs/CONTRATO_API.md](docs/CONTRATO_API.md).

## API externa

_(Pendiente: cuál se usa para la tasa USD→COP.)_

## URL pública (PaaS)

_(Pendiente)_

## Flujo de Git

`main` estable, una rama por funcionalidad y Pull Request con revisión cruzada.
_(Pendiente: documentar aquí el conflicto de merge resuelto.)_

## Colección de Postman

_(Pendiente: ruta del archivo .json en el repo.)_
