# Productos API

API REST desarrollada con Spring Boot para la administración de productos y categorías de un comercio.

Permite realizar operaciones CRUD sobre productos, consultar categorías y filtrar productos por diferentes criterios.

## Tech stack

* Java 21
* Spring Boot
* Gradle
* Spring Data JPA - Persistencia de datos y operaciones CRUD
* H2 Database - Base de datos en memoria
* Spring Validation - Validación de datos de entrada
* Spring Cache - Caché de consultas
* ModelMapper - Conversión entre entidades y DTOs
* Swagger / OpenAPI - Documentación y prueba de la API
* RestClient - Integración con el servicio externo de categorías
* Lombok

## Prerequisites

Para compilar y ejecutar el proyecto es necesario tener instalado:

* Java 21
* Gradle

## Build

Para compilar el proyecto:

### Gradle

```bash
./gradlew build
```

En Windows:

```bash
gradlew.bat build
```


## Run

### Gradle

Linux / macOS:

```bash
./gradlew bootRun
```

Windows:

```bash
gradlew.bat bootRun
```

La aplicación se ejecuta por defecto en:

```text
http://localhost:8080
```

## Database

La aplicación utiliza una base de datos H2 en memoria.

La información almacenada se mantiene mientras la aplicación está en ejecución y se pierde al reiniciarla.

Las categorías se obtienen mediante la integración con la API pública de EscuelaJS:

```text
https://api.escuelajs.co/api/v1/categories
```

Si las categorías no se encuentran disponibles en la base de datos, la aplicación las sincroniza desde el servicio externo.

## Swagger / OpenAPI

La API cuenta con documentación interactiva mediante Swagger UI.

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

Desde Swagger UI se pueden consultar y ejecutar los diferentes endpoints de la API.

## APIs

### Products

#### Obtener todos los productos

```http
GET /products
```

Permite obtener el listado de productos.

También se pueden aplicar filtros, utilizando un único filtro por solicitud.

#### Obtener un producto por ID

```http
GET /products/{id}
```

Ejemplo:

```http
GET /products/1
```

#### Crear un producto

```http
POST /products
```

Ejemplo:

```json
{
    "name": "Mouse inalámbrico",
    "price": 15000,
    "stock": 10,
    "categoryId": 2
}
```

#### Actualizar un producto

```http
PUT /products/{id}
```

Body:

```json
{
    "name": "Mouse inalámbrico actualizado",
    "price": 18000,
    "stock": 15,
    "categoryId": 2
}
```

#### Eliminar un producto

```http
DELETE /products/{id}
```


### Categories

#### Obtener todas las categorías

```http
GET /categories
```

Las categorías se almacenan en la base de datos H2 y se sincronizan con la API externa de EscuelaJS cuando es necesario.

## Validations

Los datos recibidos mediante los endpoints de productos y categorías se validan utilizando Bean Validation.

Entre las validaciones implementadas se encuentran:

* Nombre obligatorio.
* Longitud mínima y máxima del nombre.
* Precio obligatorio y mayor a cero.
* Stock obligatorio y mayor o igual a uno.
* Categoría obligatoria.


## Error handling

La aplicación utiliza excepciones personalizadas y un manejador global de excepciones.

Excepciones principales:

* `ResourceNotFoundException` - Recurso no encontrado.
* `BadRequestException` - Solicitud inválida.
* `ExternalServiceException` - Error al comunicarse con el servicio externo.

El manejo de estas excepciones se centraliza mediante `GlobalExceptionHandler`.

Los errores se devuelven utilizando respuestas HTTP apropiadas, por ejemplo:

```text
400 Bad Request
404 Not Found
503 Service Unavailable
```

Las respuestas de error utilizan un formato común mediante `ErrorResponse`.

## Cache

Se utiliza Spring Cache para evitar consultas innecesarias a la base de datos.

Las consultas que pueden ser reutilizadas se almacenan temporalmente en caché.

Cuando se actualizan o eliminan recursos, la caché correspondiente se invalida para evitar devolver información desactualizada.

## Project structure

La aplicación está organizada utilizando una arquitectura por capas:

### Config

Contiene las configuraciones de ModelMapper, caché y RestClient.


### Controller

Recibe las solicitudes HTTP y devuelve las respuestas de la API.

### Service

Contiene la lógica de negocio de la aplicación.

### Repository

Se encarga del acceso a la base de datos mediante Spring Data JPA.

### Model

Contiene las entidades utilizadas para la persistencia.

### DTO

Define los objetos utilizados para la comunicación con la API, evitando exponer directamente las entidades.

### Mapper

Se encarga de convertir entre DTOs y entidades.

### Exception

Contiene las excepciones personalizadas y el `GlobalExceptionHandler`.


## Testing

El proyecto incluye tests unitarios para verificar el comportamiento de los servicios y sus principales casos de éxito y error.

Las pruebas utilizan:

* JUnit 5
* Mockito

Los tests unitarios permiten probar la lógica de los servicios aislándolos de dependencias externas como la base de datos.

Para ejecutar los tests:

```bash
./gradlew test
```

En Windows:

```bash
gradlew.bat test
```

