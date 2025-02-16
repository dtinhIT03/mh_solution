package org.example.mhsrepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.example")
public class MhsRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MhsRepositoryApplication.class, args);
    }

}
