package com.tienda.compras.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleCompraRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private BigDecimal precioUnitario;
}