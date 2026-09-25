# InventarioBackend - StockAndes

Backend REST empresarial del caso **StockAndes, control de inventarios** (Examen Parcial U1 - Versión B).

## Tecnologías
- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA / Hibernate
- Bean Validation
- Oracle
- springdoc-openapi / Swagger UI
- Maven

## Base de datos de desarrollo
1. Conéctate como `SYSTEM` a `FREEPDB1` y ejecuta `sql/00_crear_usuario_inventariodb.sql`.
2. Verifica la conexión:
   - Host: `localhost`
   - Puerto: `1522`
   - Service Name: `FREEPDB1`
   - Usuario: `INVENTARIODB`
   - Contraseña: `1234567`
3. Ejecuta la aplicación una vez para que Hibernate cree las tablas.
4. Ejecuta `sql/datos_semilla.sql` conectado como `INVENTARIODB`.

Si tu Oracle usa el puerto 1521, cambia el puerto en `application-dev.yaml`.

## Ejecución
```bash
mvnw.cmd spring-boot:run
```

Swagger UI:
`http://localhost:8080/swagger-ui.html`

## Endpoints principales
- `GET /api/v1/health`
- CRUD `/api/v1/categorias`
- CRUD `/api/v1/productos`
- `GET /api/v1/categorias/{id}/productos`
- CRUD `/api/v1/areas`
- `POST /api/v1/despachos`
- `GET /api/v1/despachos`
- `GET /api/v1/despachos/{id}`
- `PATCH /api/v1/despachos/{id}/anular`
- `GET /api/v1/productos/buscar`
- `GET /api/v1/reportes/productos-despachados?periodo=AAAA-MM&categoriaId=`

## Reglas de negocio
- RN-01: área activa y productos activos.
- RN-02: no despachar más que el stock disponible; anular devuelve stock.
- RN-03: gasto mensual registrado + despacho nuevo no supera el presupuesto del área.
- RN-04: no repetir productos; importe = cantidad × costo unitario copiado al detalle.

Las reglas se encuentran en la capa `service/impl`, no en los controladores.

## CORS
Solo se permite `http://localhost:4200` para `/api/**`.

## Pruebas
```bash
mvnw.cmd test
```
Hay dos pruebas unitarias mínimas en `DespachoServiceImplTest`.

## Postman
Importa `postman/InventarioBackend.postman_collection.json` y ejecuta los casos del Anexo B en orden, sobre una base recién cargada.

## Solicitudes de cambio de la Parte II
SC-A, SC-B, SC-C y SC-D **no están preimplementadas** porque el PDF indica que el docente asigna una de ellas individualmente durante la evaluación y debe desarrollarse en la rama `sc-<letra>-<apellido>` con al menos tres commits.
