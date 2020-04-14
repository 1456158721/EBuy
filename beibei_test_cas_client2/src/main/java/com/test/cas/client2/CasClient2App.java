package com.test.cas.client2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.cas.common")
@ComponentScan("com.test.cas.client2")
public class CasClient2App {

    public static void main(String[] args) {
        SpringApplication.run(CasClient2App.class, args);
    }
}
