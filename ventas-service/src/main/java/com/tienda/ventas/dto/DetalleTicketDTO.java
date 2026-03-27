package com.tienda.ventas.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleTicketDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String productoCodigo;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}