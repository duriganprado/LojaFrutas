package com.fruta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruta.model.Fruta;

@Repository
public interface FrutaRepository extends JpaRepository<Fruta, Long> {
    List<Fruta> findByDisponivel(Boolean disponivel);
    List<Fruta> findByCategoriaIgnoreCase(String categoria);
    List<Fruta> findByNomeContainingIgnoreCase(String nome);
    List<Fruta> findByOrganico(Boolean organico);
    List<Fruta> findByEmPromocao(Boolean emPromocao);
}
