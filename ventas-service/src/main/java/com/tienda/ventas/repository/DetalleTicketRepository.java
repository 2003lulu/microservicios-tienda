package com.tienda.ventas.repository;

import com.tienda.ventas.model.DetalleTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleTicketRepository extends JpaRepository<DetalleTicket, Long> {
    List<DetalleTicket> findByTicketId(Long ticketId);
    List<DetalleTicket> findByProductoId(Long productoId);
}