package com.tienda.ventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VentasServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(VentasServiceApplication.class, args);
    }
}