package com.example.springreactivecosmos.coffee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CoffeeServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private CoffeeService coffeeService;
    private MockWebServer mockWebServer;
    private RedisClient redisClient;
    @MockBean
    private CoffeeRepository coffeeRepository;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        redisClient = new RedisClient(baseUrl);

        coffeeService = new CoffeeService(coffeeRepository, redisClient);
    }

    @After
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void givenCoffeeId_shouldGetCoffee() throws JsonProcessingException, InterruptedException {
        String id = "1";
        Coffee expected = Coffee.builder()
                .name("coffee_1")
                .id(id)
                .build();

        when(coffeeRepository.findById(id)).thenReturn(Mono.just(expected));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(expected)));

        StepVerifier.create(coffeeService.getCoffee(id))
                .expectNextMatches(coffee -> coffee.getId().equals(expected.getId())
                        && coffee.getName().equals(expected.getName()))
                .verifyComplete();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/coffee/1", recordedRequest.getPath());
    }

    @Test
    public void giveCoffee_shouldSaveInBackend() throws InterruptedException {
        Coffee coffee = Coffee.builder()
                .name("coffee_1")
                .id("1")
                .build();

        when(coffeeRepository.save(coffee)).thenReturn(Mono.just(coffee));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("true"));

        StepVerifier.create(coffeeService.createCoffee(coffee))
                .expectNextMatches(coffee1 -> coffee1.getId().equals(coffee.getId())
                        && coffee1.getName().equals(coffee.getName()))
                .verifyComplete();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/coffee/1/coffee_1", recordedRequest.getPath());
    }

    @Test
    public void givenCoffee_shouldDeleteInBackend() throws InterruptedException {
        Coffee coffee = Coffee.builder()
                .name("coffee_1")
                .id("1")
                .build();

        when(coffeeRepository.delete(coffee)).thenReturn(Mono.empty());

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody("true"));

        StepVerifier.create(coffeeService.deleteCoffee(coffee))
                .verifyComplete();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/coffee/1", recordedRequest.getPath());
    }


    @Test
    public void shouldGetAllCoffees() throws JsonProcessingException {
        Coffee coffee1 = Coffee.builder()
                .name("coffee_1")
                .id("1")
                .build();

        when(coffeeRepository.findAll()).thenReturn(Flux.just(coffee1));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(objectMapper.writeValueAsString(coffee1)));

        StepVerifier.create(coffeeService.getAllCoffees())
                .expectNextMatches(coffee -> coffee.getId().equals(coffee1.getId())
                        && coffee.getName().equals(coffee1.getName()))
                .verifyComplete();
    }
}