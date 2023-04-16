package com.example.springreactivecosmos.coffee;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Getter
public class RedisClient {
    private final WebClient webClient;

    public RedisClient(@Value("${redis.host}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }
}
