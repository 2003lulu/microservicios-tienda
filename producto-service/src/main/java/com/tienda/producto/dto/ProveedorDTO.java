package com.tienda.producto.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String ruc;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime createdAt;
}