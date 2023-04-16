package com.example.springreactivecosmos.coffee;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Container(containerName = "coffee", ru = "400")
public class Coffee {
    private String id;
    private String name;
}