package com.example.restapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepo myProducts,OrderRepo myOrder){
        return args -> {
            log.info("logging" +
                    myProducts.save(new Product("AirPods v3 2021","Headphones", 699.0,"great value product")));
            log.info("logging" +
                    myProducts.save(new Product("iPhone 13","Cellular", 4000.0,"brand new iphone 13 the best as always")));
            log.info("logging" +
                    myProducts.save(new Product("MacBook pro 2021","laptops", 23000.0,"thiner / faster : simply better")));
   
            
        };
    }
    @Bean
    CommandLineRunner initDatabase2 (OrderRepo myOrders){
        return args -> {
            log.info("logging" +
                    myOrders.save
                            (new Order(LocalDate.now(),"Order 1",100.0, List.of())));
            log.info("logging" +
                    myOrders.save
                            (new Order(LocalDate.now(),"Order 2",85.0, List.of())));
            log.info("logging" +
                    myOrders.save
                            (new Order(LocalDate.now(),"Order 3",90.0, List.of())));
        };
    }
    
    
    
}
