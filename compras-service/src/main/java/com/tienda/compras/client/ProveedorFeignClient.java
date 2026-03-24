package com.tienda.compras.client;

import com.tienda.compras.dto.ProveedorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service")
public interface ProveedorFeignClient {

    @GetMapping("/api/proveedores/{id}")
    ProveedorDTO obtenerProveedor(@PathVariable("id") Long id);
}