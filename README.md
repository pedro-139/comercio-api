# API REST - Administración de Productos

## Breve descripción de la solución
API REST desarrollada en Java 21 con Spring Boot para la administración de productos y categorías de un comercio, como parte del Desafío Técnico de Java de Bricks. Permite operaciones CRUD completas sobre productos, filtrado por nombre, precio, stock o categoría, y se integra con la API pública`https://api.escuelajs.co/api/v1/categories`, sincronizando los datos localmente si no existen. Utiliza H2 como base de datos en memoria, Gradle como gestor de build, cache con Spring Cache sobre la consulta de producto por ID, manejo centralizado de errores con `@RestControllerAdvice`, documentación interactiva con Swagger UI y tests unitarios con JUnit 5 + Mockito sobre la capa de servicios.

## Requisitos para ejecutar el proyecto
- Java 21.
- Conexión a internet (necesaria para sincronizar las categorías desde la API externa de Escuelajs).

## Instrucciones de compilación y ejecución
Desde la raíz del proyecto:

**Compilar**
```bash
./gradlew build # Linux/macOS
./gradlew.bat build # Windows
```

**Ejecutar los tests**
```bash
./gradlew test          # Linux/macOS
./gradlew.bat test        # Windows
```

**Ejecutar la aplicación**
```bash
./gradlew bootRun       # Linux/macOS
./gradlew.bat bootRun     # Windows
```

La aplicación queda disponible en `http://localhost:8080`.

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **Especificación OpenAPI:** `http://localhost:8080/v3/api-docs`
- **Consola H2:** `http://localhost:8080/h2-console`
  JDBC URL: `jdbc:h2:mem:productsdb` 
- · Usuario: `sa` 
- · Contraseña: (en blanco)

## Información necesaria para probar la API
La app no carga datos de prueba en forma automática al arrancar. `src/main/resources/import.txt` contiene 30 inserts de productos de ejemplo (columnas `nombre`, `precio`, `stock`, `id_categoria`) pensados para pegarse manualmente en la consola H2 después de haber llamado `GET /categories` o `GET /products` al menos una vez, ya que hacen referencia a los IDs de categoría 1 a 5.

**Endpoints — Productos**

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/products` | Listar productos (admite un filtro opcional a la vez) |
| GET | `/products/{id}` | Obtener un producto por ID |
| POST | `/products` | Crear un producto |
| PUT | `/products/{id}` | Actualizar un producto |
| DELETE | `/products/{id}` | Eliminar un producto |

**Endpoints — Categorías**

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/categories` | Listar categorías (sincroniza desde Escuelajs si no hay datos locales) |

**Filtros disponibles en `GET /products`** (uno por solicitud — enviar más de uno devuelve `400 Bad Request`):
```http
GET /products?name=Mouse
GET /products?price=25000
GET /products?stock=20
GET /products?categoryId=2
```

**Crear producto**
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

**Actualizar producto**
```json
PUT /products/1
{
  "name": "Mouse inalámbrico actualizado",
  "price": 28000,
  "stock": 15,
  "categoryId": 2
}
```
Si no existe: `404 Not Found`. Eliminar responde `204 No Content`.

**Validaciones:** nombre obligatorio y no vacío, precio obligatorio y mayor a 0, stock obligatorio (mínimo 1), `categoryId` obligatorio. Un request inválido responde `400 Bad Request`.

**Códigos de error principales:** `400`, `404`, `503` (falla el servicio externo de categorías), `500`.

**Flujo recomendado para probar (desde Swagger UI):**
1. `GET /categories` — sincroniza y consulta las categorías.
2. *(Opcional)* Pegar el contenido de `src\main\resources\import.txt` en la consola H2 (`http://localhost:8080/h2-console`) para cargar 30 productos de ejemplo, ya con las categorías disponibles.
3. `GET /products` — ver los productos cargados.
4. Probar filtros: `GET /products?categoryId=2`, `GET /products?name=Mouse Inalámbrico`.
5. `GET /products/1` dos veces seguidas — la segunda vez no imprime el mensaje de acceso a base de datos en consola, lo que confirma que respondió desde cache.
6. `POST /products` — crear uno nuevo.
7. `PUT /products/{id}` — actualizarlo (Elimina la cache de ese ID).
8. `DELETE /products/{id}` — eliminarlo (Elimina la cache de ese ID).

## Explicación de las principales decisiones técnicas
- **Arquitectura por capas** (Controller → Service → Repository → Model): Separa responsabilidades para facilitar mantenibilidad y legibilidad.
- **DTOs separados** (`ProductoRequest`, `ProductoResponse`, `CategoriaResponse`): Evitan exponer directamente las entidades JPA y controlan qué información entra y sale de la API. La conversión Entity ↔ DTO se aisló en clases `Mapper` dedicadas.
- **H2 en memoria**: Se utilizo h2 como base de datos en memoria para facilitar la ejecución y las pruebas del proyecto, sin necesidad de instalar o configurar una base de datos externa.
- **Filtro único por solicitud:** Se decidió aceptar un solo filtro (`name`, `price`, `stock` o `categoryId`) por request, devolviendo `400 Bad Request` si se envía más de uno, en lugar de combinarlos mediante una lógica más compleja.

- **RestClient** para la integración con la API pública de categorías, sincronizando automáticamente solo cuando no hay datos.
- **Jakarta Bean Validation** Para validar automáticamente los datos de entrada.
- **`@RestControllerAdvice`** (`GlobalExceptionHandler`) para centralizar el manejo de excepciones personalizadas (`ResourceNotFoundException`, `BadRequestException`, `ExternalServiceException`) con una estructura de respuesta de error común.
- **Spring Cache** sobre la consulta de producto por ID (`@Cacheable`), invalidada con `@CacheEvict` en `update` y `delete`, ya que normalemte es la operación de lectura más repetida.
