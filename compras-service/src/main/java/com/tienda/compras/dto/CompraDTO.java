package com.tienda.compras.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompraDTO {
    private Long id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
    private Long proveedorId;
    private String proveedorNombre;
    private List<DetalleCompraDTO> detalles;
}