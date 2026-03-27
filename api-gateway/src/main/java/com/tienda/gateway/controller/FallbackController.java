package com.tienda.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> usuariosFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Servicio de usuarios no disponible");
        response.put("message", "El servicio de usuarios está temporalmente fuera de servicio");
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/productos")
    public ResponseEntity<Map<String, Object>> productosFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Servicio de productos no disponible");
        response.put("message", "El servicio de productos está temporalmente fuera de servicio");
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/ventas")
    public ResponseEntity<Map<String, Object>> ventasFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Servicio de ventas no disponible");
        response.put("message", "El servicio de ventas está temporalmente fuera de servicio");
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/compras")
    public ResponseEntity<Map<String, Object>> comprasFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Servicio de compras no disponible");
        response.put("message", "El servicio de compras está temporalmente fuera de servicio");
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}