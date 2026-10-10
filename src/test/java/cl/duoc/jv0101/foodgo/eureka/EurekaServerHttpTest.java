package cl.duoc.jv0101.foodgo.eureka;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"eureka.server.use-read-only-response-cache=false"})
class EurekaServerHttpTest {
    @Autowired private TestRestTemplate http;

    @Test
    void dashboardIsAvailable() {
        ResponseEntity<String> response = http.getForEntity("/", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Instances currently registered with Eureka");
    }

    @Test
    void registryAcceptsAndRemovesInstance() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        String app = "EP02-TEST";
        String id = "ep02-test:19001";
        Map<String, Object> instance = Map.of(
                "instanceId", id, "hostName", "127.0.0.1", "app", app,
                "ipAddr", "127.0.0.1", "status", "UP",
                "port", Map.of("$", 19001, "@enabled", "true"),
                "vipAddress", app,
                "dataCenterInfo", Map.of("@class",
                        "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo", "name", "MyOwn"));
        try {
            ResponseEntity<String> created = http.postForEntity("/eureka/apps/" + app,
                    new HttpEntity<>(Map.of("instance", instance), headers), String.class);
            assertThat(created.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            ResponseEntity<String> found = http.exchange("/eureka/apps/" + app + "/" + id,
                    HttpMethod.GET, new HttpEntity<>(headers), String.class);
            assertThat(found.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(found.getBody()).contains(id).contains("UP");
        } finally {
            http.delete("/eureka/apps/" + app + "/" + id);
        }
        ResponseEntity<String> deleted = http.getForEntity("/eureka/apps/" + app + "/" + id,
                String.class);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
