package com.btb.sne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringNeo4jEscoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNeo4jEscoApplication.class, args);
    }

}
