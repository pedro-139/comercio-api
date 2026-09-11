# Productos API

API REST desarrollada con **Java 21 y Spring Boot** para la administración de productos y categorías de un comercio.

El proyecto fue desarrollado como parte del **Desafío Técnico de Java de Bricks**.

La aplicación permite realizar operaciones CRUD sobre productos, consultar categorías, filtrar productos, validar los datos recibidos, manejar errores y utilizar una integración con una API externa para obtener categorías.

---

## Tecnologías

* **Java 21**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **H2 Database**
* **Spring Validation**
* **Spring Cache**
* **RestClient**
* **Swagger / OpenAPI**
* **JUnit 5**
* **Mockito**
* **Gradle**

---

## Funcionalidades

La API permite:

* Crear productos.
* Consultar todos los productos.
* Consultar un producto por ID.
* Actualizar productos.
* Eliminar productos.
* Filtrar productos por nombre, precio, stock o categoría.
* Consultar categorías.
* Integrarse con una API externa de categorías.
* Validar los datos recibidos.
* Manejar errores mediante excepciones personalizadas.
* Utilizar caché para consultas de productos.
* Documentar y probar la API mediante Swagger UI.
* Ejecutar tests unitarios.

---

# Arquitectura

El proyecto utiliza una **arquitectura por capas**, separando las responsabilidades principales:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
H2 Database
```

Además, se utilizan DTOs, Mappers, manejo global de excepciones e integración con servicios externos.

### Estructura

```text
src/
├── main/
│   ├── java/com/bricks/productos_api/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── mapper/
│   │   ├── repository/
│   │   └── service/
│   │
│   └── resources/
│       ├── application.properties
│       └── import.sql
│
└── test/
    └── java/
```

### Controller

Recibe las solicitudes HTTP, obtiene los parámetros necesarios y delega la lógica en la capa `Service`.

No contiene la lógica principal del negocio.

### Service

Contiene la lógica de negocio de la aplicación.

Se encarga, entre otras cosas, de:

* Crear y actualizar productos.
* Verificar la existencia de categorías.
* Buscar productos.
* Aplicar filtros.
* Manejar excepciones de negocio.
* Coordinar los repositorios y otros servicios.

### Repository

Utiliza **Spring Data JPA** para acceder a la base de datos.

Los repositorios se encargan de operaciones como:

* Buscar.
* Guardar.
* Actualizar.
* Eliminar.

### Entity

Representan las tablas de la base de datos.

Las principales entidades son:

* `Producto`
* `Categoria`

Un producto posee una relación `ManyToOne` con una categoría.

### DTO

Los DTOs definen los datos que entran y salen de la API.

Se utilizan:

* `ProductoRequest`: datos necesarios para crear o actualizar un producto.
* `ProductoResponse`: información devuelta al consultar un producto.
* `CategoriaResponse`: información devuelta sobre una categoría.

Esto evita exponer directamente las entidades JPA.

### Mapper

Los Mappers se encargan de transformar:

```text
Entity → DTO
DTO → Entity
```

De esta forma, la conversión de objetos queda separada de la lógica de negocio.

### Exception

Contiene las excepciones personalizadas y el `GlobalExceptionHandler`.

Permite centralizar el manejo de errores y devolver respuestas HTTP adecuadas.

---

# Base de datos

La aplicación utiliza **H2 como base de datos en memoria**.

Esto significa que:

* No es necesario instalar un motor de base de datos externo.
* Las tablas son creadas automáticamente mediante JPA.
* Los datos se pierden al detener la aplicación.

Para facilitar las pruebas, el proyecto incluye:

```text
src/main/resources/import.sql
```

Este archivo contiene datos iniciales de categorías y productos.

Al iniciar la aplicación, H2 carga automáticamente estos datos, permitiendo comenzar a utilizar los endpoints inmediatamente desde Swagger, Postman o cualquier otro cliente HTTP.

---

# Datos iniciales

El archivo `import.sql` contiene categorías y **30 productos de prueba**.

Los productos están distribuidos principalmente entre las categorías:

```text
1 - Clothes
2 - Electronics
3 - Furniture
4 - Shoes
5 - Miscellaneous
```

Se da mayor importancia a las primeras categorías, especialmente `Clothes`, `Electronics`, `Furniture` y `Shoes`, para que los filtros por categoría puedan probarse con una cantidad suficiente de productos.

Los datos iniciales son únicamente datos de prueba y pueden modificarse o eliminarse mediante los endpoints correspondientes.

---

# Integración con API externa

Las categorías del proyecto se basan en la API pública de EscuelaJS:

```text
https://api.escuelajs.co/api/v1/categories
```

La aplicación utiliza `RestClient` para comunicarse con este servicio.

La integración permite obtener información de categorías externas y utilizar sus identificadores dentro de la aplicación.

Las categorías iniciales utilizadas por `import.sql` corresponden a los primeros IDs disponibles del servicio externo, permitiendo que los productos de prueba mantengan una relación válida con sus categorías.

---

# Swagger / OpenAPI

La API cuenta con documentación interactiva mediante Swagger UI.

Una vez iniciada la aplicación:

```text
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger se pueden:

* Consultar los endpoints.
* Ver los modelos de request y response.
* Ejecutar solicitudes HTTP.
* Probar filtros.
* Probar validaciones y respuestas de error.

La especificación OpenAPI también está disponible en:

```text
http://localhost:8080/v3/api-docs
```

---

# Endpoints

## Productos

### Listar productos

```http
GET /products
```

Devuelve todos los productos.

También permite filtrar por un atributo:

```text
name
price
stock
categoryId
```

### Filtrar por nombre

```http
GET /products?name=Mouse
```

### Filtrar por precio

```http
GET /products?price=25000
```

### Filtrar por stock

```http
GET /products?stock=20
```

### Filtrar por categoría

```http
GET /products?categoryId=2
```

> La implementación actual permite utilizar **un filtro por solicitud**, ya que la consigna solicita búsquedas o filtros por nombre, precio, stock y categoría, pero no exige combinarlos.

---

## Obtener producto por ID

```http
GET /products/{id}
```

Ejemplo:

```http
GET /products/1
```

Si el producto no existe:

```text
404 Not Found
```

---

## Crear producto

```http
POST /products
```

Ejemplo:

```json
{
  "name": "Mouse inalámbrico",
  "price": 25000,
  "stock": 20,
  "categoryId": 2
}
```

Respuesta exitosa:

```text
201 Created
```

---

## Actualizar producto

```http
PUT /products/{id}
```

Ejemplo:

```http
PUT /products/1
```

Body:

```json
{
  "name": "Mouse inalámbrico actualizado",
  "price": 28000,
  "stock": 15,
  "categoryId": 2
}
```

Si el producto no existe:

```text
404 Not Found
```

---

## Eliminar producto

```http
DELETE /products/{id}
```

Ejemplo:

```http
DELETE /products/1
```

Respuesta exitosa:

```text
204 No Content
```

---

# Categorías

## Listar categorías

```http
GET /categories
```

Devuelve las categorías disponibles en la aplicación.

---

# Validaciones

Los requests de productos utilizan **Jakarta Bean Validation**.

Entre las validaciones implementadas:

* El nombre es obligatorio.
* El nombre no puede estar vacío.
* El precio es obligatorio.
* El precio debe ser válido según las reglas definidas.
* El stock es obligatorio.
* El stock debe cumplir la cantidad mínima definida.
* El ID de categoría es obligatorio.

Ejemplo de request inválido:

```json
{
  "name": "",
  "price": -100,
  "stock": 0,
  "categoryId": null
}
```

La API responde:

```text
400 Bad Request
```

Las validaciones se ejecutan mediante `@Valid` y los errores son manejados por el `GlobalExceptionHandler`.

---

# Manejo de errores

La aplicación utiliza excepciones personalizadas para representar diferentes tipos de errores.

Principales excepciones:

* `ResourceNotFoundException`
* `BadRequestException`
* `ExternalServiceException`

El `GlobalExceptionHandler`, mediante `@RestControllerAdvice`, centraliza el tratamiento de las excepciones.

Principales códigos utilizados:

```text
400 Bad Request
404 Not Found
503 Service Unavailable
500 Internal Server Error
```

Las respuestas de error utilizan una estructura común mediante `ErrorResponse`.

---

# Caché

Se utiliza **Spring Cache** para almacenar temporalmente determinadas consultas.

Actualmente, la consulta de un producto por ID utiliza:

```java
@Cacheable(value = "productosCache", key = "#id")
```

Esto permite que una segunda consulta al mismo producto pueda obtener la información desde la caché sin volver a consultar la base de datos.

Cuando un producto es actualizado o eliminado, la caché correspondiente se invalida mediante `@CacheEvict`.

Esto evita mantener en caché información desactualizada.

---

# Tests

El proyecto utiliza:

* **JUnit 5**
* **Mockito**

Los tests unitarios permiten comprobar la lógica de los servicios sin depender de una base de datos real.

Entre los casos contemplados se encuentran:

* Creación de productos.
* Consulta por ID.
* Producto inexistente.
* Actualización.
* Eliminación.
* Filtros.
* Manejo de excepciones.

Para ejecutar los tests:

### Windows

```bash
gradlew.bat test
```

También se puede ejecutar:

```bash
gradlew.bat clean test
```

---

# Requisitos

Para ejecutar el proyecto se necesita:

* **Java 21**
* Sistema operativo compatible con Java.
* Conexión a Internet para utilizar la integración con la API externa.

No es necesario instalar:

* MySQL.
* PostgreSQL.
* H2 por separado.
* Gradle de forma global.

El proyecto incluye el **Gradle Wrapper**.

---

# Ejecución

Desde la raíz del proyecto:

### Windows

```bash
gradlew.bat bootRun
```

La aplicación se ejecutará en:

```text
http://localhost:8080
```

Una vez iniciada, los productos incluidos en `import.sql` estarán disponibles para realizar pruebas.

---

# Compilación

Para compilar:

```bash
gradlew.bat build
```

Para limpiar y compilar:

```bash
gradlew.bat clean build
```

Para ejecutar los tests:

```bash
gradlew.bat test
```

---

# Consola H2

Mientras la aplicación está ejecutándose, se puede acceder a la consola H2 desde:

```text
http://localhost:8080/h2-console
```

La URL JDBC y las credenciales utilizadas se encuentran en:

```text
src/main/resources/application.properties
```

---

# Flujo recomendado para probar la aplicación

Una vez iniciado el proyecto:

### 1. Consultar los productos

```http
GET /products
```

Se pueden observar los productos cargados automáticamente mediante `import.sql`.

### 2. Probar filtros

Por ejemplo:

```http
GET /products?categoryId=2
```

o:

```http
GET /products?name=Mouse
```

### 3. Obtener un producto

```http
GET /products/1
```

### 4. Crear un producto

```http
POST /products
```

Con:

```json
{
  "name": "Producto de prueba",
  "price": 50000,
  "stock": 10,
  "categoryId": 2
}
```

### 5. Actualizarlo

```http
PUT /products/{id}
```

### 6. Eliminarlo

```http
DELETE /products/{id}
```

### 7. Consultar categorías

```http
GET /categories
```

Todos estos pasos pueden realizarse directamente desde Swagger UI.

---

# Decisiones técnicas principales

### Arquitectura por capas

Se separaron Controllers, Services, Repositories, Entities, DTOs, Mappers y Exceptions para mantener responsabilidades claras y facilitar el mantenimiento.

### DTOs separados

Se utilizan `ProductoRequest` y `ProductoResponse` en lugar de exponer directamente las entidades.

Esto permite controlar qué información recibe y devuelve la API.

### H2

Se eligió H2 porque el desafío solicita una base de datos en memoria y permite ejecutar el proyecto sin instalar un motor externo.

### Integración externa

Se utiliza `RestClient` para comunicarse con la API pública de categorías.

### Validaciones

Se utilizan anotaciones de Jakarta Bean Validation para validar automáticamente los datos recibidos por la API.

### Manejo global de errores

Se centralizó el tratamiento de excepciones mediante `@RestControllerAdvice`, evitando repetir lógica de manejo de errores en cada Controller.

### Caché

Se utiliza Spring Cache para reducir consultas repetidas a la base de datos en operaciones de consulta por ID.

### Datos iniciales

Se incorporó `import.sql` para que el proyecto pueda ejecutarse inmediatamente con productos de prueba, facilitando la evaluación de los endpoints sin tener que crear manualmente todos los productos.

---

# Estado del proyecto

* ✅ API REST de productos.
* ✅ CRUD de productos.
* ✅ Consulta de categorías.
* ✅ Filtros de productos.
* ✅ Integración con API externa.
* ✅ Base de datos H2.
* ✅ Datos iniciales mediante `import.sql`.
* ✅ DTOs Request / Response.
* ✅ Mappers.
* ✅ Validaciones.
* ✅ Manejo global de excepciones.
* ✅ Caché.
* ✅ Swagger / OpenAPI.
* ✅ Tests unitarios.
* ✅ Documentación.

---

## Autor

**Pedro Serrano**

Desafío técnico de Java — Bricks.
