package com.tienda.producto.service;

import com.tienda.producto.dto.ProveedorDTO;
import com.tienda.producto.model.Proveedor;
import com.tienda.producto.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public List<ProveedorDTO> obtenerTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProveedorDTO> obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public ProveedorDTO crearProveedor(ProveedorDTO request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(request.getNombre());
        proveedor.setRuc(request.getRuc());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());
        proveedor.setDireccion(request.getDireccion());

        Proveedor saved = proveedorRepository.save(proveedor);
        return convertToDTO(saved);
    }

    @Transactional
    public Optional<ProveedorDTO> actualizarProveedor(Long id, ProveedorDTO request) {
        return proveedorRepository.findById(id)
                .map(proveedor -> {
                    proveedor.setNombre(request.getNombre());
                    proveedor.setRuc(request.getRuc());
                    proveedor.setTelefono(request.getTelefono());
                    proveedor.setEmail(request.getEmail());
                    proveedor.setDireccion(request.getDireccion());
                    return convertToDTO(proveedorRepository.save(proveedor));
                });
    }

    @Transactional
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    private ProveedorDTO convertToDTO(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setRuc(proveedor.getRuc());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEmail(proveedor.getEmail());
        dto.setDireccion(proveedor.getDireccion());
        dto.setCreatedAt(proveedor.getCreatedAt());
        return dto;
    }
}