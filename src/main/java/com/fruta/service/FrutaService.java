package com.fruta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruta.model.Fruta;
import com.fruta.repository.FrutaRepository;

@Service
@Transactional
public class FrutaService {
    
    @Autowired
    private FrutaRepository frutaRepository;
    
    public Fruta salvar(Fruta fruta) {
        if (fruta.getId() == null) {
            fruta.setDataCadastro(LocalDateTime.now());
        }
        return frutaRepository.save(fruta);
    }
    
    public List<Fruta> listarTodas() {
        return frutaRepository.findAll();
    }
    
    public List<Fruta> listarDisponiveis() {
        return frutaRepository.findByDisponivel(true);
    }
    
    public List<Fruta> buscarPorCategoria(String categoria) {
        return frutaRepository.findByCategoriaIgnoreCase(categoria);
    }
    
    public List<Fruta> buscarPorNome(String nome) {
        return frutaRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Fruta> listarOrganicas() {
        return frutaRepository.findByOrganico(true);
    }
    
    public List<Fruta> listarEmPromocao() {
        return frutaRepository.findByEmPromocao(true);
    }
    
    public Optional<Fruta> buscarPorId(Long id) {
        return frutaRepository.findById(id);
    }
    
    public Fruta atualizar(Fruta fruta) {
        return frutaRepository.save(fruta);
    }
    
    public void deletar(Long id) {
        frutaRepository.deleteById(id);
    }
    
    public void marcarComoIndisponivel(Long id) {
        Fruta fruta = frutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fruta não encontrada"));
        fruta.setDisponivel(false);
        frutaRepository.save(fruta);
    }
    
    public void marcarComoDisponivel(Long id) {
        Fruta fruta = frutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fruta não encontrada"));
        fruta.setDisponivel(true);
        frutaRepository.save(fruta);
    }
    
    public void atualizarEstoque(Long id, Integer novaQuantidade) {
        Fruta fruta = frutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fruta não encontrada"));
        fruta.setEstoque(novaQuantidade);
        frutaRepository.save(fruta);
    }
    
    public void diminuirEstoque(Long id, Integer quantidade) {
        Fruta fruta = frutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fruta não encontrada"));
        
        if (fruta.getEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }
        
        fruta.setEstoque(fruta.getEstoque() - quantidade);
        frutaRepository.save(fruta);
    }
}
