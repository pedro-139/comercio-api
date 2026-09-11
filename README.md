# Productos API

API REST de Productos y Categorías — Java / Spring Boot

## Descripción

Este proyecto implementa una API REST para la administración de productos y categorías de un comercio, desarrollada como parte del **Desafío Técnico de Java de Bricks**.

Permite realizar operaciones CRUD sobre productos, consultar categorías, filtrar productos, validar los datos recibidos, manejar errores, usar caché e integrarse con una API externa para obtener categorías.

## Características principales

- CRUD completo de productos
- Consulta de categorías
- Integración con API externa (escuelajs)
- Caché de resultados
- Manejo global de errores
- Tests unitarios (services)
- Swagger UI (OpenAPI 3)
- Arquitectura por capas

## Tecnologías principales

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot | Framework principal |
| Spring Web | Exposición de API REST |
| Spring Data JPA | Persistencia |
| H2 Database | Base en memoria |
| Spring Cache | Cache de resultados |
| Spring Validation | Validación de requests (Jakarta Bean Validation) |
| springdoc-openapi | Swagger UI |
| RestClient | Integración con API externa |
| Gradle (Wrapper) | Build tool |
| JUnit 5 + Mockito | Tests automatizados |

## Objetivos del sistema

**CRUD de productos**

- Crear producto
- Obtener producto por ID
- Listar todos (con filtros)
- Actualizar producto
- Eliminar producto

**Filtros disponibles en `/products`**

- `name`
- `price`
- `stock`
- `categoryId`

La implementación actual admite un filtro por solicitud, no combinaciones de varios a la vez.

**Categorías**

- Listar todas
- Sincronizar/obtener desde la API externa si no hay datos locales

## Integración externa — escuelajs API

Las categorías se obtienen desde la API pública:

```
https://api.escuelajs.co/api/v1/categories
```

Si no existen categorías guardadas localmente, la aplicación las sincroniza automáticamente desde este servicio al consultar `GET /categories`. Las categorías iniciales que trae `import.sql` corresponden a los primeros IDs disponibles del servicio externo (con más peso en Clothes, Electronics, Furniture y Shoes), para tener suficientes productos de prueba a la hora de probar los filtros por categoría.

## Documentación

Swagger UI, una vez levantada la app:

```
http://localhost:8080/swagger-ui/index.html
```

Especificación OpenAPI:

```
http://localhost:8080/v3/api-docs
```

## Caché

La consulta de producto por ID usa caché:

- `@Cacheable(value = "productosCache", key = "#id")` en la consulta por ID.
- `@CacheEvict` cuando un producto se actualiza o elimina, para no dejar en caché información desactualizada.

Se eligió aplicar la caché sobre la consulta por ID porque es la operación de lectura que más se repite en un flujo típico de uso.

Para poder verificar a simple vista que la caché está funcionando, `findById` deja un `System.out.println` justo antes de ir a la base de datos: si se consulta el mismo ID dos veces, el mensaje solo aparece la primera vez (la segunda responde directo desde caché sin tocar el repository). Es intencional, pensado para que sea fácil de comprobar durante la evaluación.

## Tests

JUnit 5 + Mockito, sobre la capa de servicios:

- `ProductoServiceImplTest`: creación, listado sin filtro, listado filtrando por nombre/precio/stock/categoría, búsqueda por ID (existente e inexistente), actualización (existente e inexistente) y eliminación (existente e inexistente).
- `CategoriaServiceImplTest`: listado con datos, listado sincronizando cuando la base está vacía, búsqueda por ID (con dato, sincronizando si no existe, y 404 si tampoco aparece luego de sincronizar).

Para ejecutar:

```powershell
.\gradlew.bat clean test
```

## Requisitos para ejecutar el proyecto

- Java 21
- Sistema operativo compatible con Java
- Conexión a Internet, para la integración con la API externa de categorías

No hace falta instalar Gradle (el proyecto incluye el **Gradle Wrapper**) ni una base de datos externa (se usa **H2 en memoria**).

## Compilación y ejecución

Desde la raíz del proyecto (Windows):

**Compilar**

```powershell
.\gradlew.bat build
```

Limpiar y recompilar:

```powershell
.\gradlew.bat clean build
```

**Ejecutar los tests**

```powershell
.\gradlew.bat test
```

**Ejecutar la aplicación**

```powershell
.\gradlew.bat bootRun
```

La aplicación queda disponible en:

```
http://localhost:8080
```

**Consola H2**

Con la app corriendo:

```
http://localhost:8080/h2-console
```

La URL JDBC y las credenciales están en `src/main/resources/application.properties`.

## Información necesaria para probar la API

### Datos de prueba

La app usa H2 en memoria, así que los datos se pierden al detener la aplicación. Se cargan automáticamente al iniciar mediante `src/main/resources/import.sql`, que trae 30 productos de prueba.

### Endpoints — Productos

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/products` | Listar productos (con filtros opcionales) |
| GET | `/products/{id}` | Obtener un producto por ID |
| POST | `/products` | Crear un producto |
| PUT | `/products/{id}` | Actualizar un producto |
| DELETE | `/products/{id}` | Eliminar un producto |

Filtros (uno por solicitud):

```http
GET /products?name=Mouse
GET /products?price=25000
GET /products?stock=20
GET /products?categoryId=2
```

Crear producto:

```json
POST /products
{
  "name": "Mouse inalámbrico",
  "price": 25000,
  "stock": 20,
  "categoryId": 2
}
```

Respuesta: `201 Created`.

Actualizar producto:

```json
PUT /products/1
{
  "name": "Mouse inalámbrico actualizado",
  "price": 28000,
  "stock": 15,
  "categoryId": 2
}
```

Si el producto no existe: `404 Not Found`. Eliminar responde `204 No Content`.

### Endpoints — Categorías

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/categories` | Listar categorías (sincronizadas desde escuelajs si no hay datos locales) |

### Validaciones

Los requests de producto usan Jakarta Bean Validation:

- Nombre obligatorio, no puede estar vacío.
- Precio obligatorio y mayor a 0.
- Stock obligatorio, mínimo 1.
- `categoryId` obligatorio.

Ejemplo de request inválido:

```json
{
  "name": "",
  "price": -100,
  "stock": 0,
  "categoryId": null
}
```

Respuesta: `400 Bad Request`.

### Manejo de errores

Excepciones personalizadas (`ResourceNotFoundException`, `BadRequestException`, `ExternalServiceException`) centralizadas en `GlobalExceptionHandler` (`@RestControllerAdvice`), con una estructura de respuesta común (`ErrorResponse`).

Códigos principales: `400`, `404`, `503`, `500`.

### Flujo recomendado para probar

1. `GET /products` — ver los productos cargados por `import.sql`.
2. Probar filtros: `GET /products?categoryId=2`, `GET /products?name=Mouse`.
3. `GET /products/1` — obtener un producto puntual, y repetir la consulta para ver la caché en acción (el segundo llamado no imprime el log de acceso a base de datos).
4. `POST /products` — crear uno nuevo.
5. `PUT /products/{id}` — actualizarlo.
6. `DELETE /products/{id}` — eliminarlo.
7. `GET /categories` — consultar categorías.

Todo esto se puede hacer directamente desde Swagger UI.

## Arquitectura

```
Controller
    ↓
Service
    ↓
Repository
    ↓
Model
    ↓
H2 Database
```

```
src/
├── main/
│   ├── java/com/bricks/productos_api/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── model/
│   │   ├── exception/
│   │   ├── mapper/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── application.properties
│       └── import.sql
└── test/
    └── java/
```

- **Controller**: recibe las solicitudes HTTP y delega en el Service, sin lógica de negocio.
- **Service**: lógica de negocio (crear/actualizar productos, verificar categorías, aplicar filtros).
- **Repository**: acceso a datos con Spring Data JPA.
- **Model**: entidades `Producto` y `Categoria`, con relación ManyToOne de Producto hacia Categoria.
- **DTO**: `ProductoRequest`, `ProductoResponse`, `CategoriaResponse`, para no exponer directamente las entidades JPA.
- **Mapper**: conversión Entity ↔ DTO, separada de la lógica de negocio.
- **Exception**: excepciones personalizadas y el `GlobalExceptionHandler`.

## Decisiones técnicas principales

- **Arquitectura por capas**: separa Controllers, Services, Repositories, Entities, DTOs, Mappers y Exceptions para mantener responsabilidades claras.
- **DTOs separados**: evitan exponer directamente las entidades JPA y controlan qué información entra y sale de la API.
- **H2 en memoria**: el desafío pide una base en memoria; permite ejecutar el proyecto sin instalar un motor externo.
- **RestClient** para la integración con la API pública de categorías (escuelajs), la indicada en la consigna.
- **Jakarta Bean Validation** para validar automáticamente los datos recibidos.
- **`@RestControllerAdvice`** para centralizar el manejo de errores y no repetir lógica en cada Controller.
- **Spring Cache** sobre la consulta de producto por ID, invalidada con `@CacheEvict` en update/delete.
- **`import.sql`** con productos de prueba (con más peso en Clothes, Electronics, Furniture y Shoes) para poder probar los endpoints y filtros sin cargar todo a mano.

## Estado del proyecto

- API REST de productos y categorías
- CRUD completo
- Filtros de productos
- Integración con API externa
- Base de datos H2
- Datos iniciales (`import.sql`)
- DTOs Request/Response + Mappers
- Validaciones
- Manejo global de excepciones
- Caché
- Swagger / OpenAPI
- Tests unitarios
- Documentación

## Autor

Pedro Serrano — Desafío técnico de Java, Bricks.