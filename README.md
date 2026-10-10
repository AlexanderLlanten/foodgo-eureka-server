# FoodGo Eureka Server

Registro de los ocho microservicios FoodGo para la demostración desde VS Code. Infraestructura de descubrimiento solicitada por los estudiantes; el CRUD y las relaciones JPA pertenecen a los ocho dominios.

## Ejecutar

Requisitos: Java 21 y Maven 3.9+.

```powershell
git clone https://github.com/jhoramirez-afk/foodgo-eureka-server.git
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
