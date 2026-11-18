# FoodGo Eureka Server

Registro de los ocho microservicios FoodGo para la demostración desde VS Code. Infraestructura de descubrimiento solicitada por los estudiantes; el CRUD y las relaciones JPA pertenecen a los ocho dominios.

## Ejecutar

Requisitos: Java 21 y Maven 3.9+.

```powershell
git clone https://github.com/AlexanderLlanten/foodgo-eureka-server.git
Set-Location foodgo-eureka-server
mvn clean
mvn install
mvn package
java -Xms64m -Xmx384m -jar target/foodgo-eureka-server-2.0.0.jar
```

Panel: http://127.0.0.1:8761/
Registro JSON: GET http://127.0.0.1:8761/eureka/apps con Accept application/json.

Iniciar este servidor antes que los servicios, y arrancar cada uno con --spring.profiles.active=mysql,eureka. El nombre y puerto de cada instancia proceden de spring.application.name y server.port. Esperar a que las ocho aparezcan UP y comprobar también sus rutas con Postman.

Eureka guarda el catálogo en memoria y recibe heartbeats. No almacena usuarios, pedidos o pagos y no necesita una BD JPA. Ctrl+C cierra el JAR. Las pruebas usan un servidor en puerto aleatorio y comprueban el panel y el registro; sus instancias de prueba nunca se publican en el servidor de demostración.

Docker opcional: docker compose up --build -d --wait --wait-timeout 300.

Referencias: [Spring Cloud Netflix](https://docs.spring.io/spring-cloud-netflix/docs/4.1.6/reference/html/), [compatibilidad](https://github.com/spring-cloud/spring-cloud-release/wiki/Supported-Versions).

## EP03: AWS Academy y Docker Swarm

Las imágenes base del Dockerfile están fijadas por digest. El build ejecuta Maven y pruebas antes de empaquetar la imagen Java 21.

El servidor se ejecuta en el overlay privado del cluster y mantiene el registro de los ocho clientes. El dashboard no se expone como ruta administrativa pública.

El CI de este repositorio valida el proyecto en cada cambio. El CD central de [foodgo-arquitectura](https://github.com/jhoramirez-afk/foodgo-arquitectura/blob/main/docs/EP03-Despliegue-AWS.md) fija los SHA de los nueve proyectos, construye imágenes en ECR, aprovisiona AWS con CloudFormation y despliega mediante SSM. Ejecutar ese pipeline para publicar una nueva versión del conjunto; no hay despliegue cloud automático desde este repositorio.
