package coffee;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import redis.embedded.RedisServer;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CoffeeControllerTest {

    private static final RedisServer REDISSERVER = new RedisServer(6379);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveRedisOperations<String, Coffee> redisOperations;
    private String key = UUID.randomUUID().toString();
    private String name = "coffee_1";

    @BeforeAll
    static void startUpRedisServer() {
        REDISSERVER.start();
    }

    @AfterAll
    static void shutDownRedisServer() {
        REDISSERVER.stop();
    }

    @BeforeEach
    void setUp() {
        redisOperations.opsForValue().set(key, Coffee.builder()
                        .id(key)
                        .name(name)
                        .build())
                .subscribe();
    }

    @AfterEach
    void destroy() {
        redisOperations.opsForValue().delete(key).subscribe();
    }

    @Test
    void getAll() {
        
        webTestClient.get()
                .uri("/coffees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody()
                .jsonPath("$.*", hasSize(4));
    }

    @Test
    void get() {

        webTestClient.get()
                .uri("/coffee/" + key)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody()
                .jsonPath("$.name").isEqualTo(name)
                .jsonPath("$.id").isEqualTo(key);
    }

    @Test
    void delete() {
        
        StepVerifier.create(redisOperations.keys("*"))
                .expectNextCount(4)
                .verifyComplete();

        webTestClient.delete()
                .uri("/coffee/" + key)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

        StepVerifier.create(redisOperations.keys("*"))
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void add() {

        StepVerifier.create(redisOperations.keys("*"))
                .expectNextCount(4)
                .verifyComplete();

        webTestClient.post()
                .uri("/coffee/" + UUID.randomUUID().toString() + "/" + name)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED);
    }
}