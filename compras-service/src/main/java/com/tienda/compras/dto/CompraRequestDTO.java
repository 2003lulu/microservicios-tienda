package com.tienda.compras.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class CompraRequestDTO {

    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long proveedorId;

    @Size(min = 1, message = "La compra debe tener al menos un detalle")
    private List<DetalleCompraRequestDTO> detalles;
}