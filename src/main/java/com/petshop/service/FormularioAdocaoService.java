package com.petshop.service;

import com.petshop.model.FormularioAdocao;
import com.petshop.model.Usuario;
import com.petshop.repository.FormularioAdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormularioAdocaoService {
    
    @Autowired
    private FormularioAdocaoRepository formularioRepository;
    
    @Autowired
    private AnimalService animalService;
    
    public FormularioAdocao salvar(FormularioAdocao formulario) {
        if (formulario.getId() == null) {
            formulario.setDataSolicitacao(LocalDateTime.now());
            formulario.setStatus(FormularioAdocao.StatusAdocao.PENDENTE);
        }
        return formularioRepository.save(formulario);
    }
    
    public List<FormularioAdocao> listarTodos() {
        return formularioRepository.findAll();
    }
    
    public List<FormularioAdocao> listarPorUsuario(Usuario usuario) {
        return formularioRepository.findByUsuario(usuario);
    }
    
    public List<FormularioAdocao> listarPorStatus(FormularioAdocao.StatusAdocao status) {
        return formularioRepository.findByStatus(status);
    }
    
    public List<FormularioAdocao> listarPorAnimal(Long animalId) {
        return formularioRepository.findByAnimalId(animalId);
    }
    
    public Optional<FormularioAdocao> buscarPorId(Long id) {
        return formularioRepository.findById(id);
    }
    
    public FormularioAdocao aprovar(Long id, String observacoes) {
        FormularioAdocao formulario = formularioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado"));
        
        formulario.setStatus(FormularioAdocao.StatusAdocao.APROVADO);
        formulario.setDataResposta(LocalDateTime.now());
        formulario.setObservacoes(observacoes);
        
        // Marcar animal como adotado
        animalService.marcarComoAdotado(formulario.getAnimal().getId());
        
        return formularioRepository.save(formulario);
    }
    
    public FormularioAdocao recusar(Long id, String observacoes) {
        FormularioAdocao formulario = formularioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado"));
        
        formulario.setStatus(FormularioAdocao.StatusAdocao.RECUSADO);
        formulario.setDataResposta(LocalDateTime.now());
        formulario.setObservacoes(observacoes);
        
        return formularioRepository.save(formulario);
    }
    
    public void cancelar(Long id) {
        FormularioAdocao formulario = formularioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado"));
        
        formulario.setStatus(FormularioAdocao.StatusAdocao.CANCELADO);
        formulario.setDataResposta(LocalDateTime.now());
        
        formularioRepository.save(formulario);
    }
    
    public void deletar(Long id) {
        formularioRepository.deleteById(id);
    }
}
