package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example", "org.kie.kogito"})
public class KogitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KogitoApplication.class, args);
    }

}
