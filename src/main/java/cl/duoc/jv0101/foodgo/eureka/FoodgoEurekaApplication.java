package cl.duoc.jv0101.foodgo.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FoodgoEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodgoEurekaApplication.class, args);
    }
}
