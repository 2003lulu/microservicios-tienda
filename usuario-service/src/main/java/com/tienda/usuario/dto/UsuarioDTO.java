package com.tienda.usuario.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private String rol;
    private LocalDateTime createdAt;
    private Boolean activo;
}