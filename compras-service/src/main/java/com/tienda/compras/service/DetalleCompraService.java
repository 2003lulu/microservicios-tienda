package com.tienda.compras.service;

import com.tienda.compras.dto.DetalleCompraDTO;
import com.tienda.compras.repository.DetalleCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleCompraService {

    private final DetalleCompraRepository detalleCompraRepository;

    public List<DetalleCompraDTO> obtenerPorCompra(Long compraId) {
        return detalleCompraRepository.findByCompraId(compraId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DetalleCompraDTO convertToDTO(com.tienda.compras.model.DetalleCompra detalle) {
        DetalleCompraDTO dto = new DetalleCompraDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProductoId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}