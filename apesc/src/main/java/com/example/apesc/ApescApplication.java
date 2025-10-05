package com.example.apesc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApescApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApescApplication.class, args);
    }

}
