package com.example.springreactivecosmos;

import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringReactiveCosmosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveCosmosApplication.class, args);
    }

}
