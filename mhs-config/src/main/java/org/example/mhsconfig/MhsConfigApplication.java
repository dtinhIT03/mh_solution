package org.example.mhsconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.example.mhsconfig")
public class MhsConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(MhsConfigApplication.class, args);
    }

}
