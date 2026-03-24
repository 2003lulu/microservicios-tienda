package com.tienda.compras.repository;

import com.tienda.compras.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByProveedorId(Long proveedorId);
    List<Compra> findByEstado(String estado);
    List<Compra> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}