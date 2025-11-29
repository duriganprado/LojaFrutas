package com.fruta.security;

import com.fruta.model.Cliente;
import com.fruta.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado: " + email));
        
        if (!cliente.getAtivo()) {
            throw new UsernameNotFoundException("Cliente inativo: " + email);
        }
        
        Set<GrantedAuthority> authorities = cliente.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
        
        return User.builder()
                .username(cliente.getEmail())
                .password(cliente.getSenha())
                .authorities(authorities)
                .build();
    }
}
