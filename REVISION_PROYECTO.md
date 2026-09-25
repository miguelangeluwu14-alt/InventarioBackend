# Revisión del proyecto

El proyecto fue adaptado al caso StockAndes del Examen Parcial U1 - Versión B.

Verificado estáticamente:
- paquetes y nombres del caso InventarioBackend;
- expresiones regulares Java con escapes correctos;
- XML del pom.xml válido;
- YAML de perfiles dev/prod válido;
- JSON de colección Postman válido;
- ausencia de Spring Security, JWT, Pageable, Spring Data REST y System.out;
- cuatro reglas de negocio ubicadas en la capa de servicio;
- 15 casos del Anexo B representados en la colección Postman;
- datos semilla completos: 3 categorías, 12 productos y 6 áreas.

No fue posible ejecutar Maven en este entorno porque Maven Wrapper no pudo descargar Maven 3.9.16 desde Maven Central. El proyecto debe abrirse en IntelliJ con Java 21 y acceso a internet para resolver dependencias y ejecutar `mvnw.cmd test`.
