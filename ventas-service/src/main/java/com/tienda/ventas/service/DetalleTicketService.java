package com.tienda.ventas.service;

import com.tienda.ventas.dto.DetalleTicketDTO;
import com.tienda.ventas.repository.DetalleTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleTicketService {

    private final DetalleTicketRepository detalleTicketRepository;

    public List<DetalleTicketDTO> obtenerPorTicket(Long ticketId) {
        return detalleTicketRepository.findByTicketId(ticketId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DetalleTicketDTO convertToDTO(com.tienda.ventas.model.DetalleTicket detalle) {
        DetalleTicketDTO dto = new DetalleTicketDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProductoId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());

        return dto;
    }
}