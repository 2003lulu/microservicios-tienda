package com.tienda.ventas.service;

import com.tienda.ventas.client.ClienteFeignClient;
import com.tienda.ventas.client.ProductoFeignClient;
import com.tienda.ventas.dto.*;
import com.tienda.ventas.model.DetalleTicket;
import com.tienda.ventas.model.Ticket;
import com.tienda.ventas.repository.DetalleTicketRepository;
import com.tienda.ventas.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final DetalleTicketRepository detalleTicketRepository;
    private final ClienteFeignClient clienteFeignClient;
    private final ProductoFeignClient productoFeignClient;

    public List<TicketDTO> obtenerTodos() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TicketDTO> obtenerPorId(Long id) {
        return ticketRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public TicketDTO crearTicket(TicketRequestDTO request) {
        // Validar cliente
        ClienteDTO cliente = clienteFeignClient.obtenerCliente(request.getClienteId());
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado");
        }

        // Crear ticket
        Ticket ticket = new Ticket();
        ticket.setClienteId(request.getClienteId());
        ticket.setUsuarioId(request.getUsuarioId());
        ticket.setEstado("COMPLETADO");
        ticket.setTotal(BigDecimal.ZERO);

        Ticket savedTicket = ticketRepository.save(ticket);
        BigDecimal total = BigDecimal.ZERO;

        // Procesar detalles
        for (DetalleTicketRequestDTO detalleReq : request.getDetalles()) {
            ProductoDTO producto = productoFeignClient.obtenerProducto(detalleReq.getProductoId());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + detalleReq.getProductoId());
            }

            if (producto.getStock() < detalleReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para producto: " + producto.getNombre());
            }

            // Actualizar stock
            productoFeignClient.actualizarStock(detalleReq.getProductoId(), -detalleReq.getCantidad());

            // Crear detalle
            DetalleTicket detalle = new DetalleTicket();
            detalle.setTicket(savedTicket);
            detalle.setProductoId(detalleReq.getProductoId());
            detalle.setCantidad(detalleReq.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(detalleReq.getCantidad())));

            detalleTicketRepository.save(detalle);
            total = total.add(detalle.getSubtotal());
        }

        // Actualizar total del ticket
        savedTicket.setTotal(total);
        ticketRepository.save(savedTicket);

        return convertToDTO(savedTicket);
    }

    @Transactional
    public void anularTicket(Long id) {
        ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setEstado("ANULADO");
                    // Revertir stock
                    for (DetalleTicket detalle : ticket.getDetalles()) {
                        productoFeignClient.actualizarStock(detalle.getProductoId(), detalle.getCantidad());
                    }
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setFecha(ticket.getFecha());
        dto.setTotal(ticket.getTotal());
        dto.setEstado(ticket.getEstado());
        dto.setClienteId(ticket.getClienteId());
        dto.setUsuarioId(ticket.getUsuarioId());

        // Obtener nombre del cliente
        try {
            ClienteDTO cliente = clienteFeignClient.obtenerCliente(ticket.getClienteId());
            if (cliente != null) {
                dto.setClienteNombre(cliente.getNombre() + " " + (cliente.getApellido() != null ? cliente.getApellido() : ""));
            }
        } catch (Exception e) {
            dto.setClienteNombre("No disponible");
        }

        if (ticket.getDetalles() != null) {
            List<DetalleTicketDTO> detallesDTO = ticket.getDetalles().stream()
                    .map(this::convertDetalleToDTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }

        return dto;
    }

    private DetalleTicketDTO convertDetalleToDTO(DetalleTicket detalle) {
        DetalleTicketDTO dto = new DetalleTicketDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProductoId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        try {
            ProductoDTO producto = productoFeignClient.obtenerProducto(detalle.getProductoId());
            if (producto != null) {
                dto.setProductoNombre(producto.getNombre());
                dto.setProductoCodigo(producto.getCodigo());
            }
        } catch (Exception e) {
            dto.setProductoNombre("No disponible");
        }

        return dto;
    }
}