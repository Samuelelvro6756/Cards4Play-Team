# Contrato de la API (acordar entre los 3 antes de codificar)

Autenticación: HTTP Basic (`Authorization: Basic ...`).
Formato de error en TODA la API: `{"error": "mensaje"}` con el código HTTP correcto.
Las respuestas con precios en COP incluyen `"fuenteTasa": "API"` o `"DEFECTO"`.

## Códigos HTTP

| Código | Cuándo |
|---|---|
| 200 / 201 | Consulta exitosa / recurso creado |
| 400 | Datos mal formados o faltantes |
| 401 | Sin autenticación o credenciales incorrectas |
| 403 | Autenticado pero sin permiso (cliente haciendo algo de admin, abrir un booster ajeno) |
| 404 | Recurso inexistente |
| 409 | Conflicto: booster ya vendido o abierto, torneo lleno, cliente ya inscrito, usuario duplicado |

## Endpoints

| Área | Endpoint | Quién puede | Responsable |
|---|---|---|---|
| Clientes | `POST /clientes` | Admin | A |
| | `GET /clientes` | Admin | A |
| Productos | `POST /productos` (el JSON indica si es carta, sellado o accesorio) | Admin | B |
| | `GET /productos`, `GET /productos/{id}` | Autenticado | B |
| Boosters | `POST /boosters` | Admin | B |
| | `GET /boosters/disponibles` (sin mostrar cartas) | Autenticado | B |
| | `POST /boosters/{id}/abrir` | Solo su dueño | B |
| Compras | `POST /compras` (lista de ítems) | Cliente | C |
| | `GET /compras`, `GET /compras/{id}` | Cliente (las suyas) | C |
| Torneos | `POST /torneos` | Admin | C |
| | `GET /torneos` | Autenticado | C |
| | `POST /torneos/{id}/inscripciones` | Cliente | C |
| Tasa | `GET /tasa` | Autenticado | C |
| Salud | `GET /salud` | Público | A |

## Persistencia

Archivos JSON en `DATA_DIR` (por defecto `data/`): `usuarios.json`, `productos.json`, `torneos.json`, `compras.json`.
Variables de entorno: `TASA_DEFECTO`, `DATA_DIR`, `PORT`.
