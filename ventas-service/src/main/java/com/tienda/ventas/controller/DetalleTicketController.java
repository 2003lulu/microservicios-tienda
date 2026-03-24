package com.tienda.ventas.controller;

import com.tienda.ventas.dto.DetalleTicketDTO;
import com.tienda.ventas.service.DetalleTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detalle-ticket")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DetalleTicketController {

    private final DetalleTicketService detalleTicketService;

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<DetalleTicketDTO>> obtenerPorTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(detalleTicketService.obtenerPorTicket(ticketId));
    }
}