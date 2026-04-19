package com.vetadopcion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.vetcommon.model")
@EnableJpaRepositories(basePackages = "com.vetcommon.repository")
public class ModuloAdopcionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuloAdopcionApplication.class, args);
    }
}