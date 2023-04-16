package com.example.springreactivecosmos.coffee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController("/coffee")
@RequiredArgsConstructor
public class CoffeeController {

    private final CoffeeService coffeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Coffee> createCoffee(@RequestBody Coffee coffee) {
        return coffeeService.createCoffee(coffee);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Coffee> getCoffee(@PathVariable String id) {
        return coffeeService.getCoffee(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteCoffee(@RequestBody Coffee coffee) {
        return coffeeService.deleteCoffee(coffee);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Coffee> getAllCoffees() {
        return coffeeService.getAllCoffees();
    }
}
