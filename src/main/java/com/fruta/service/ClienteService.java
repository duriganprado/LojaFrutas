package com.fruta.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruta.model.Cliente;
import com.fruta.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Cliente registrarCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        
        // Define role padrão
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        cliente.setRoles(roles);
        
        return clienteRepository.save(cliente);
    }
    
    public Cliente criarAdmin(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");
        cliente.setRoles(roles);
        
        return clienteRepository.save(cliente);
    }
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public Cliente atualizar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
    
    public void alterarSenha(Long id, String novaSenha) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        cliente.setSenha(passwordEncoder.encode(novaSenha));
        clienteRepository.save(cliente);
    }
}
