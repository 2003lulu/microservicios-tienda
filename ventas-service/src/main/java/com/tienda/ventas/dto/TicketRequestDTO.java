package com.tienda.ventas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class TicketRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @Size(min = 1, message = "El ticket debe tener al menos un detalle")
    private List<DetalleTicketRequestDTO> detalles;
}