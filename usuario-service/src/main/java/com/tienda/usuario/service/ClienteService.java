package com.tienda.usuario.service;

import com.tienda.usuario.dto.ClienteDTO;
import com.tienda.usuario.dto.ClienteRequestDTO;
import com.tienda.usuario.model.Cliente;
import com.tienda.usuario.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteDTO> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClienteDTO> obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public ClienteDTO crearCliente(ClienteRequestDTO request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setDireccion(request.getDireccion());

        return convertToDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public Optional<ClienteDTO> actualizarCliente(Long id, ClienteRequestDTO request) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(request.getNombre());
                    cliente.setApellido(request.getApellido());
                    cliente.setTelefono(request.getTelefono());
                    cliente.setEmail(request.getEmail());
                    cliente.setDireccion(request.getDireccion());
                    return convertToDTO(clienteRepository.save(cliente));
                });
    }

    @Transactional
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setDireccion(cliente.getDireccion());
        dto.setCreatedAt(cliente.getCreatedAt());
        return dto;
    }
}