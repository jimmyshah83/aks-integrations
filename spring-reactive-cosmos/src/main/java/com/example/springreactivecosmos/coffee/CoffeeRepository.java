package com.example.springreactivecosmos.coffee;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeRepository extends ReactiveCosmosRepository<Coffee, String> {
}
