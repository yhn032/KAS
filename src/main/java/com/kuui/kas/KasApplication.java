package com.kuui.kas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KasApplication {

    public static void main(String[] args) {
        SpringApplication.run(KasApplication.class, args);
    }

}
