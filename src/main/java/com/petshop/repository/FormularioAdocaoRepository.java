package com.petshop.repository;

import com.petshop.model.FormularioAdocao;
import com.petshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormularioAdocaoRepository extends JpaRepository<FormularioAdocao, Long> {
    List<FormularioAdocao> findByUsuario(Usuario usuario);
    List<FormularioAdocao> findByStatus(FormularioAdocao.StatusAdocao status);
    List<FormularioAdocao> findByAnimalId(Long animalId);
}
