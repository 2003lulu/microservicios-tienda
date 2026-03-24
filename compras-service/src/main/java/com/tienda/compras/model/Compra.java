package com.tienda.compras.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Column(precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleCompra> detalles;

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
    }
}