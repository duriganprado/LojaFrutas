package com.petshop.service;

import com.petshop.model.Animal;
import com.petshop.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnimalService {
    
    @Autowired
    private AnimalRepository animalRepository;
    
    public Animal salvar(Animal animal) {
        if (animal.getId() == null) {
            animal.setDataCadastro(LocalDateTime.now());
        }
        return animalRepository.save(animal);
    }
    
    public List<Animal> listarTodos() {
        return animalRepository.findAll();
    }
    
    public List<Animal> listarDisponiveis() {
        return animalRepository.findByDisponivelAdocao(true);
    }
    
    public List<Animal> buscarPorEspecie(String especie) {
        return animalRepository.findByEspecieIgnoreCase(especie);
    }
    
    public List<Animal> buscarPorNome(String nome) {
        return animalRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Optional<Animal> buscarPorId(Long id) {
        return animalRepository.findById(id);
    }
    
    public Animal atualizar(Animal animal) {
        return animalRepository.save(animal);
    }
    
    public void deletar(Long id) {
        animalRepository.deleteById(id);
    }
    
    public void marcarComoAdotado(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        animal.setDisponivelAdocao(false);
        animalRepository.save(animal);
    }
    
    public void marcarComoDisponivel(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        animal.setDisponivelAdocao(true);
        animalRepository.save(animal);
    }
}
