package com.tienda.usuario.service;

import com.tienda.usuario.dto.UsuarioDTO;
import com.tienda.usuario.dto.UsuarioRequestDTO;
import com.tienda.usuario.model.Usuario;
import com.tienda.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<UsuarioDTO> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    @Transactional
    public UsuarioDTO crearUsuario(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username ya existe");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(request.getPassword());
        usuario.setEmail(request.getEmail());
        usuario.setRol(request.getRol() != null ? request.getRol() : "USER");
        usuario.setActivo(true);

        return convertToDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public Optional<UsuarioDTO> actualizarUsuario(Long id, UsuarioRequestDTO request) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setUsername(request.getUsername());
                    usuario.setEmail(request.getEmail());
                    usuario.setRol(request.getRol());
                    return convertToDTO(usuarioRepository.save(usuario));
                });
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setCreatedAt(usuario.getCreatedAt());
        dto.setActivo(usuario.getActivo());
        return dto;
    }
}