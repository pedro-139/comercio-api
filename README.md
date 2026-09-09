# Productos API

API REST en Spring Boot para administrar productos, con persistencia H2 en memoria y categorías obtenidas desde `https://api.escuelajs.co/api/v1/categories`.

La aplicación está organizada en capas: controllers para HTTP, services para reglas
de negocio, repositories para persistencia y entities para el modelo.

## Requisitos

- Java 21
- Gradle Wrapper

## Ejecución

```bash
./gradlew bootRun
```

En Windows:

```powershell
.\gradlew.bat bootRun
```

## Endpoints

- `GET /products?name=shirt&price=20&stock=5&category=1`
- `GET /products/{id}`
- `POST /products`
- `PUT /products/{id}`
- `DELETE /products/{id}`
- `GET /categories`

El cuerpo de alta y modificación usa, por ejemplo:

```json
{
  "name": "Producto de ejemplo",
  "price": 19.99,
  "stock": 10,
  "category": { "id": 1 }
}
```

Las categorías se consultan una vez por ejecución y quedan cacheadas en memoria. Al crear o modificar un producto, si la categoría aún no está cacheada se consulta automáticamente al servicio externo.

## Decisiones técnicas

- **Base de datos:** H2 en memoria; los datos se reinician al detener la aplicación.
- **Categorías:** se consulta la API pública indicada, se guardan en H2 para validar referencias de productos y se cachean con `@Cacheable` durante la ejecución.
- **Filtros:** `name` realiza búsqueda parcial sin distinguir mayúsculas; `price`, `stock` y `category` realizan coincidencia exacta.
- **Validaciones:** nombre obligatorio, precio no negativo, stock no negativo y categoría obligatoria.
- **Errores:** recursos inexistentes responden `404`, datos inválidos `400` y errores del proveedor externo `502`.
- **Documentación:** los controllers, services y repositories incluyen comentarios sobre sus responsabilidades.
