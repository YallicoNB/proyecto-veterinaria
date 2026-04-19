package com.vetusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.vetcommon.model")
@EnableJpaRepositories(basePackages = "com.vetcommon.repository")
public class ModuloUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuloUsuariosApplication.class, args);
    }
}