package com.example.springreactivecosmos.coffee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;
    private final RedisClient redisClient;

    public Mono<Coffee> createCoffee(Coffee coffee) {
        return redisClient.getWebClient()
                .post()
                .uri("/coffee/" + coffee.getId() + "/" + coffee.getName())
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(retVal -> {
                    if (retVal) {
                        return coffeeRepository.save(coffee);
                    } else {
                        return Mono.error(new RuntimeException("Failed to create coffee"));
                    }
                });
    }

    public Mono<Coffee> getCoffee(String id) {
        return redisClient.getWebClient()
                .get()
                .uri("/coffee/" + id)
                .retrieve()
                .bodyToMono(Coffee.class)
                .switchIfEmpty(coffeeRepository.findById(id));
    }

    public Mono<Void> deleteCoffee(Coffee coffee) {
        return redisClient.getWebClient()
                .delete()
                .uri("/coffee/" + coffee.getId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(retVal -> {
                    if (retVal) {
                        return coffeeRepository.delete(coffee);
                    } else {
                        return Mono.error(new RuntimeException("Failed to delete coffee"));
                    }
                });
    }

    public Flux<Coffee> getAllCoffees() {
        return redisClient.getWebClient()
                .get()
                .uri("/coffees")
                .retrieve()
                .bodyToFlux(Coffee.class)
                .switchIfEmpty(coffeeRepository.findAll());
    }
}
