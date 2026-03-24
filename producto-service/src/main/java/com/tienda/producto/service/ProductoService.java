package com.tienda.producto.service;

import com.tienda.producto.dto.ProductoDTO;
import com.tienda.producto.dto.ProductoRequestDTO;
import com.tienda.producto.model.Categoria;
import com.tienda.producto.model.Producto;
import com.tienda.producto.repository.CategoriaRepository;
import com.tienda.producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductoDTO> obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<ProductoDTO> obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo)
                .map(this::convertToDTO);
    }

    public List<ProductoDTO> obtenerPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductoDTO crearProducto(ProductoRequestDTO request) {
        if (productoRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("El código de producto ya existe");
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setCodigo(request.getCodigo());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock() != null ? request.getStock() : 0);

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        Producto saved = productoRepository.save(producto);
        return convertToDTO(saved);
    }

    @Transactional
    public Optional<ProductoDTO> actualizarProducto(Long id, ProductoRequestDTO request) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(request.getNombre());
                    producto.setDescripcion(request.getDescripcion());
                    producto.setPrecio(request.getPrecio());
                    producto.setStock(request.getStock());

                    if (request.getCategoriaId() != null) {
                        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                        producto.setCategoria(categoria);
                    }

                    return convertToDTO(productoRepository.save(producto));
                });
    }

    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Transactional
    public void actualizarStock(Long id, Integer cantidad) {
        productoRepository.findById(id)
                .map(producto -> {
                    int nuevoStock = producto.getStock() + cantidad;
                    if (nuevoStock < 0) {
                        throw new RuntimeException("Stock insuficiente");
                    }
                    producto.setStock(nuevoStock);
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    private ProductoDTO convertToDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setCodigo(producto.getCodigo());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCreatedAt(producto.getCreatedAt());

        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }

        return dto;
    }
}