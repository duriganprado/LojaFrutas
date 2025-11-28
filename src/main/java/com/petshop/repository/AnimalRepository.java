package com.petshop.repository;

import com.petshop.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByDisponivelAdocao(Boolean disponivel);
    List<Animal> findByEspecieIgnoreCase(String especie);
    List<Animal> findByNomeContainingIgnoreCase(String nome);
}
