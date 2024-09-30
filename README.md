

# Microservicio - Gestión de Usuarios

## 1\. Requisitos Previos

Antes de comenzar, asegúrate de tener instalados los siguientes elementos en tu sistema:

- **Docker**: [Instalar Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Instalar Docker Compose](https://docs.docker.com/compose/install/)
- **Java 17+**: Necesario para ejecutar la aplicación Spring Boot.

## 2\. Configuración de Base de Datos (PostgreSQL)

Este proyecto utiliza una base de datos **PostgreSQL** que se ejecuta a través de un contenedor Docker. La configuración del contenedor está definida en el archivo `docker-compose.yml`.

### Pasos para iniciar la base de datos:

1.  **Clona el repositorio del proyecto**:
    
    `git clone https://github.com/usuario/proyecto.git cd UserManagementSystem`
2.  **Inicia el contenedor de Docker**:

    Asegúrate de estar en la raíz del proyecto donde está ubicado el archivo `docker-compose.yml`, luego ejecuta el siguiente comando para levantar la base de datos en Docker:


    `docker-compose up -d`

    Esto iniciará el contenedor de PostgreSQL y la base de datos estará lista para conectarse a la aplicación.


## 3\. Ejecutar Aplicación

Una vez que los contenedores de Docker estén funcionando y las variables de entorno configuradas, puedes inicializar la aplicación.

1.  **Construir el proyecto**:

    Ejecuta el siguiente comando para compilar la aplicación y descargar las dependencias necesarias:

    `./mvnw clean install`

2.  **Iniciar la aplicación**:

    Usa el siguiente comando para levantar el servidor de Spring Boot:

    `./mvnw spring-boot:run`

## 4\. Importar Colección de Postman

Para importar la colección de Postman y hacer uso de las rutas, sigue estos pasos:
1.  Abrir Postman y en file o archivos seleccionar "Import".

2.  Selecciona el archivo : `Microservice-UserManagment.postman_collection` que se encuentra alojado en la carpeta raiz de este proyecto.

3. La colección se importará junto a su documentación y estará lista para ser utilizada.

## 5\. Test

Para ejecutar los tests del proyecto, usa el siguiente comando:


`./mvnw test`

## 6\. Detener y Eliminar la Base de Datos

Una vez terminado de trabajar con la aplicación, puedes detener y eliminar el contenedor de PostgreSQL con los siguientes comandos:

1.  **Detener el contenedor**:



    `docker stop user-microservice`

2.  **Eliminar el contenedor**:


    `docker rm user-microservice`