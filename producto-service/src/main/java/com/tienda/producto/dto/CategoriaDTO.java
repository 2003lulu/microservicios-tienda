package com.tienda.producto.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime createdAt;
}