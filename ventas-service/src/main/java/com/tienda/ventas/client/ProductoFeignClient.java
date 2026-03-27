package com.tienda.ventas.client;

import com.tienda.ventas.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "producto-service", url = "http://localhost:8082")
public interface ProductoFeignClient {

    @GetMapping("/api/productos/{id}")
    ProductoDTO obtenerProducto(@PathVariable("id") Long id);

    @PatchMapping("/api/productos/{id}/stock")
    void actualizarStock(@PathVariable("id") Long id, @RequestParam("cantidad") Integer cantidad);
}