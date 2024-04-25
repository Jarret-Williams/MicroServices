package com.example.Microservices.config;


import com.example.Microservices.models.Product;
import com.example.Microservices.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args -> {
            Product bread = new Product(
                    "Bread",
                    "Whole bread loaf",
                    12.50,
                    25
            );
            Product milk = new Product(
                    "Milk",
                    "full cream milk",
                    20,
                    25
            );
            Product Tea = new Product(
                    "Tea",
                    "tea time",
                    20,
                    25
            );
            Product Coffee = new Product(
                    "Coffee",
                    "black coffee",
                    20,
                    25
            );
            Product Bags = new Product(
                    "Bags",
                    "bag",
                    20,
                    25
            );
            Product Socks = new Product(
                    "Socks",
                    "full wool",
                    20,
                    25
            );
            Product mouthwash = new Product(
                    "Mouthwash",
                    "listerine",
                    20,
                    25
            );
            Product HairGel = new Product(
                    "Hair Gel",
                    "clear",
                    20,
                    25
            );
            Product Shampoo = new Product(
                    "Shampoo",
                    "emerald",
                    20,
                    25
            );


            productRepository.saveAll(
                    List.of(Tea,Coffee,bread,milk,Bags,HairGel,Shampoo,mouthwash,Socks)
            );
        };
    }
}
