package com.tienda.compras.controller;

import com.tienda.compras.dto.CompraDTO;
import com.tienda.compras.dto.CompraRequestDTO;
import com.tienda.compras.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listarTodos() {
        return ResponseEntity.ok(compraService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> obtenerPorId(@PathVariable Long id) {
        return compraService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearCompra(@Valid @RequestBody CompraRequestDTO request) {
        try {
            CompraDTO compra = compraService.crearCompra(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(compra);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<?> anularCompra(@PathVariable Long id) {
        try {
            compraService.anularCompra(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}