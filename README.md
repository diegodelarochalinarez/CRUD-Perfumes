# CRUD Perfumes para prueba Softtek
Este proyecto es una API RESTful desarrollada con Spring Boot. Incluye caracteristicas como seguridad con JWT, control de concurrencia y cache distribuida.
## Estructura del Proyecto
El código fuente sigue una arquitectura en capas clásica de Spring Boot, organizada por paquetes según su responsabilidad:

*   `src/main/java/com/diego/crud`
    *   **config/**: Contiene la configuración de seguridad y beans de la aplicación.
    *   **controller/**: Capa de presentación (API REST).
    *   **service/**: Capa de lógica de negocio.
    *   **repository/**: Capa de acceso a datos.
    *   **model/**: Entidades de persistencia.
    *   **dto/**: Objetos de Transferencia de Datos.

## Ejecucion del Proyecto

### Prerrequisitos
*   **Java 17** o superior (JDK).
*   **Docker** y **Docker Compose** (para la base de datos PostgreSQL y Redis).
*   **Gradle**.

### 1. Configuracion de Variables de Entorno

La aplicacion requiere ciertas variables de entorno para conectarse a la base de datos y a Redis.
### 2. Gestion de Dependencias

Este proyecto utiliza **Gradle** para la gestion de dependencias. 

### 3. Pasos para Ejecutar

1.  **Clonar el repositorio** en tu maquina local.

2.  **Iniciar la infraestructura (Base de datos y Cache)**:
    Utiliza Docker Compose para levantar los servicios de soporte.
    ```bash
    docker-compose up -d
    ```
    Esto iniciara un contenedor de PostgreSQL en el puerto 5432 y uno de Redis en el puerto 6379.

3.  **Ejecutar la aplicacion Spring Boot**:
    Usa el wrapper de Gradle para compilar y ejecutar.

    *   **En Windows (CMD/PowerShell)**:
        ```cmd
        ./gradlew bootRun
        ```
    *   **En Linux/macOS**:
        ```bash
        ./gradlew bootRun
        ```
4.  **Verificar funcionamiento**:
    Una vez que veas el log de "Started CrudApplication", la API estara disponible en:
    `http://localhost:8080`

---

## Caracteristicas de Seguridad

La seguridad de la aplicacion se gestiona a traves de **Spring Security** y se basa en una arquitectura **Stateless** (sin estado) utilizando **JWT (JSON Web Tokens)**.

*   **Autenticacion JWT**: Cada peticion a los endpoints protegidos debe incluir un token JWT valido en el encabezado `Authorization` (formato `Bearer <token>`).
*   **Endpoints Publicos**:
    *   `/api/auth/**`: Rutas para inicio de sesion.
    *   `/error`: Manejo de errores.
*   **Proteccion CSRF**: Deshabilitada, ya que la API es stateless y no utiliza cookies de sesion para la autenticacion.

---

## Control de Concurrencia

Para garantizar la integridad de los datos se utiliza un bloqueo pesimista durante la busqueda, equivalente al lock for update en SQL y se realiza dentro de una transaccion lo que garantiza la atomicidad de la operacion.

---

## Testing

El proyecto cuenta con una suite de pruebas automatizadas para asegurar la calidad del codigo. Se utilizan **JUnit 5** para la definicion de pruebas y **Mockito** para el aislamiento de componentes.

### Ejecucion de Pruebas

Para ejecutar todas las pruebas del proyecto, utiliza el wrapper de Gradle desde la terminal:

```bash
./gradlew test
```

### Reportes

Tras la ejecucion, se genera un reporte detallado en formato HTML. Puedes consultarlo abriendo el siguiente archivo en tu navegador:

`build/reports/tests/test/index.html`

---

## Documentacion de la API

Esta API permite gestionar un inventario de perfumes, proporcionando operaciones CRUD (Crear, Leer, Actualizar, Eliminar) y autenticacion.

## Configuracion Base

*   **URL Base**: `http://localhost:8080/api`
*   **Formato de Datos**: JSON

## Modelo de Datos (Perfume)

| Campo  | Tipo    | Descripcion                          |
| :----- | :------ | :----------------------------------- |
| `id`     | Number  | Identificador unico (autogenerado)   |
| `nombre` | String  | Nombre del perfume                   |
| `marca`  | String  | Marca del fabricante                 |
| `precio` | Number  | Precio del producto (decimal)        |
| `stock`  | Number  | Cantidad disponible en inventario    |

---

## Endpoints de Autenticacion

### 1. Iniciar Sesion

Autentica a un usuario y devuelve un token JWT.

*   **Metodo**: `POST`
*   **URL**: `/auth/login`
*   **Cuerpo de la Peticion (JSON)**:

```json
{
  "username": "usuario",
  "password": "password123"
}
```

*   **Respuesta Exitosa (200 OK)**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## Endpoints de Perfumes

**Nota**: Todos los siguientes endpoints requieren el encabezado `Authorization: Bearer <token>`.

### 1. Obtener todos los perfumes

Recupera la lista completa de perfumes registrados.

*   **Metodo**: `GET`
*   **URL**: `/perfumes`
*   **Respuesta Exitosa (200 OK)**:

```json
[
  {
    "id": 1,
    "nombre": "Acqua Di Gio",
    "marca": "Giorgio Armani",
    "precio": 85.50,
    "stock": 100
  },
  {
    "id": 2,
    "nombre": "Sauvage",
    "marca": "Dior",
    "precio": 92.00,
    "stock": 50
  }
]
```

### 2. Obtener un perfume por ID

Busca un perfume especifico por su identificador unico.

*   **Metodo**: `GET`
*   **URL**: `/perfumes/{id}`
*   **Ejemplo**: `/perfumes/1`
*   **Respuesta Exitosa (200 OK)**:

```json
{
  "id": 1,
  "nombre": "Acqua Di Gio",
  "marca": "Giorgio Armani",
  "precio": 85.50,
  "stock": 100
}
```

### 3. Crear un nuevo perfume

Registra un nuevo perfume en la base de datos.

*   **Metodo**: `POST`
*   **URL**: `/perfumes`
*   **Cuerpo de la Peticion (JSON)**:
    *   Nota: No es necesario enviar el `id`.

```json
{
  "nombre": "Bleu de Chanel",
  "marca": "Chanel",
  "precio": 110.00,
  "stock": 30
}
```

*   **Respuesta Exitosa (200 OK)**: Devuelve el objeto creado con su nuevo `id`.

### 4. Actualizar un perfume

Actualiza los datos de un perfume existente.

*   **Metodo**: `PUT`
*   **URL**: `/perfumes/{id}`
*   **Ejemplo**: `/perfumes/1`
*   **Cuerpo de la Peticion (JSON)**:

```json
{
  "id": 1,
  "nombre": "Acqua Di Gio Profumo",
  "marca": "Giorgio Armani",
  "precio": 95.00,
  "stock": 80
}
```

*   **Respuesta Exitosa (200 OK)**: Devuelve el objeto actualizado.

### 5. Eliminar un perfume

Elimina un perfume del registro.

*   **Metodo**: `DELETE`
*   **URL**: `/perfumes/{id}`
*   **Ejemplo**: `/perfumes/1`
*   **Respuesta Exitosa (204 No Content)**: Sin contenido.

---
