package com.tienda.ventas.repository;

import com.tienda.ventas.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByClienteId(Long clienteId);
    List<Ticket> findByUsuarioId(Long usuarioId);
    List<Ticket> findByEstado(String estado);
    List<Ticket> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}