package com.tienda.compras.service;

import com.tienda.compras.client.ProductoFeignClient;
import com.tienda.compras.client.ProveedorFeignClient;
import com.tienda.compras.dto.*;
import com.tienda.compras.model.Compra;
import com.tienda.compras.model.DetalleCompra;
import com.tienda.compras.repository.CompraRepository;
import com.tienda.compras.repository.DetalleCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProveedorFeignClient proveedorFeignClient;
    private final ProductoFeignClient productoFeignClient;

    public List<CompraDTO> obtenerTodos() {
        return compraRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CompraDTO> obtenerPorId(Long id) {
        return compraRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public CompraDTO crearCompra(CompraRequestDTO request) {
        // Validar proveedor
        ProveedorDTO proveedor = proveedorFeignClient.obtenerProveedor(request.getProveedorId());
        if (proveedor == null) {
            throw new RuntimeException("Proveedor no encontrado");
        }

        // Crear compra
        Compra compra = new Compra();
        compra.setProveedorId(request.getProveedorId());
        compra.setEstado("COMPLETADA");
        compra.setTotal(BigDecimal.ZERO);

        Compra savedCompra = compraRepository.save(compra);
        BigDecimal total = BigDecimal.ZERO;

        // Procesar detalles
        for (DetalleCompraRequestDTO detalleReq : request.getDetalles()) {
            ProductoDTO producto = productoFeignClient.obtenerProducto(detalleReq.getProductoId());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + detalleReq.getProductoId());
            }

            // Actualizar stock (sumar al stock existente)
            productoFeignClient.actualizarStock(detalleReq.getProductoId(), detalleReq.getCantidad());

            // Crear detalle
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(savedCompra);
            detalle.setProductoId(detalleReq.getProductoId());
            detalle.setCantidad(detalleReq.getCantidad());
            detalle.setPrecioUnitario(detalleReq.getPrecioUnitario());
            detalle.setSubtotal(detalleReq.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleReq.getCantidad())));

            detalleCompraRepository.save(detalle);
            total = total.add(detalle.getSubtotal());
        }

        // Actualizar total de la compra
        savedCompra.setTotal(total);
        compraRepository.save(savedCompra);

        return convertToDTO(savedCompra);
    }

    @Transactional
    public void anularCompra(Long id) {
        compraRepository.findById(id)
                .map(compra -> {
                    compra.setEstado("ANULADA");
                    // Revertir stock (restar lo que se había sumado)
                    for (DetalleCompra detalle : compra.getDetalles()) {
                        productoFeignClient.actualizarStock(detalle.getProductoId(), -detalle.getCantidad());
                    }
                    return compraRepository.save(compra);
                })
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    private CompraDTO convertToDTO(Compra compra) {
        CompraDTO dto = new CompraDTO();
        dto.setId(compra.getId());
        dto.setFecha(compra.getFecha());
        dto.setTotal(compra.getTotal());
        dto.setEstado(compra.getEstado());
        dto.setProveedorId(compra.getProveedorId());

        // Obtener nombre del proveedor
        try {
            ProveedorDTO proveedor = proveedorFeignClient.obtenerProveedor(compra.getProveedorId());
            if (proveedor != null) {
                dto.setProveedorNombre(proveedor.getNombre());
            }
        } catch (Exception e) {
            dto.setProveedorNombre("No disponible");
        }

        if (compra.getDetalles() != null) {
            List<DetalleCompraDTO> detallesDTO = compra.getDetalles().stream()
                    .map(this::convertDetalleToDTO)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }

        return dto;
    }

    private DetalleCompraDTO convertDetalleToDTO(DetalleCompra detalle) {
        DetalleCompraDTO dto = new DetalleCompraDTO();
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