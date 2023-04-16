package coffee;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CoffeeController {
    private final ReactiveRedisOperations<String, Coffee> redisOperations;

    CoffeeController(ReactiveRedisOperations<String, Coffee> redisOperations) {
        this.redisOperations = redisOperations;
    }

    @GetMapping("/coffees")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Coffee> getAll() {
        return redisOperations
                .keys("*")
                .flatMap(redisOperations.opsForValue()::get);
    }

    @GetMapping("/coffee/{key}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Coffee> get(@PathVariable String key) {
        return redisOperations
                .keys("*")
                .flatMap(redisOperations.opsForValue()::get)
                .filter(coffee -> coffee.getId().equalsIgnoreCase(key))
                .singleOrEmpty();
    }

    @DeleteMapping("/coffee/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> delete(@PathVariable String id) {
        return redisOperations
                .keys("*")
                .flatMap(redisOperations.opsForValue()::get)
                .filter(coffee -> coffee.getId().equalsIgnoreCase(id))
                .singleOrEmpty()
                .flatMap(coffee -> redisOperations.opsForValue().delete(coffee.getId()))
                .single();
    }

    @PostMapping("/coffee/{id}/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Boolean> add(@PathVariable String name, @PathVariable String id) {
        return redisOperations.opsForValue()
                .set(id, Coffee.builder()
                        .id(id)
                        .name(name)
                        .build())
                .single();
    }
}
