package com.tienda.producto.service;

import com.tienda.producto.dto.CategoriaDTO;
import com.tienda.producto.model.Categoria;
import com.tienda.producto.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> obtenerTodos() {
        return categoriaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoriaDTO> obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public CategoriaDTO crearCategoria(CategoriaDTO request) {
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("La categoría ya existe");
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        Categoria saved = categoriaRepository.save(categoria);
        return convertToDTO(saved);
    }

    @Transactional
    public Optional<CategoriaDTO> actualizarCategoria(Long id, CategoriaDTO request) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(request.getNombre());
                    categoria.setDescripcion(request.getDescripcion());
                    return convertToDTO(categoriaRepository.save(categoria));
                });
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO convertToDTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setCreatedAt(categoria.getCreatedAt());
        return dto;
    }
}