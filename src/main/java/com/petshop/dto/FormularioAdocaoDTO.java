package com.petshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FormularioAdocaoDTO {
    
    @NotNull(message = "ID do animal é obrigatório")
    private Long animalId;
    
    private String nome;
    private String email;
    private String telefone;
    
    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;
    
    private String motivoAdocao;
    private String experienciaAnimais;
    
    // Getters and Setters
    public Long getAnimalId() {
        return animalId;
    }
    
    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getMotivoAdocao() {
        return motivoAdocao;
    }
    
    public void setMotivoAdocao(String motivoAdocao) {
        this.motivoAdocao = motivoAdocao;
    }
    
    public String getExperienciaAnimais() {
        return experienciaAnimais;
    }
    
    public void setExperienciaAnimais(String experienciaAnimais) {
        this.experienciaAnimais = experienciaAnimais;
    }
}
