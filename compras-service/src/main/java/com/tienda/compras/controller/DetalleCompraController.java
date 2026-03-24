package com.tienda.compras.controller;

import com.tienda.compras.dto.DetalleCompraDTO;
import com.tienda.compras.service.DetalleCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detalle-compra")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DetalleCompraController {

    private final DetalleCompraService detalleCompraService;

    @GetMapping("/compra/{compraId}")
    public ResponseEntity<List<DetalleCompraDTO>> obtenerPorCompra(@PathVariable Long compraId) {
        return ResponseEntity.ok(detalleCompraService.obtenerPorCompra(compraId));
    }
}