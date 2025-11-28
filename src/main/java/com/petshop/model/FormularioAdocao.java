package com.petshop.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "formularios_adocao")
public class FormularioAdocao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Usuário é obrigatório")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    @NotNull(message = "Animal é obrigatório")
    private Animal animal;
    
    @NotBlank(message = "Endereço é obrigatório")
    @Column(nullable = false)
    private String endereco;
    
    @Column(name = "possui_outros_pets")
    private Boolean possuiOutrosPets = false;
    
    @Column(name = "tipo_residencia")
    private String tipoResidencia; // Casa, Apartamento
    
    @Column(name = "tem_quintal")
    private Boolean temQuintal = false;
    
    @Column(name = "motivo_adocao", length = 500)
    private String motivoAdocao;
    
    @Column(name = "experiencia_pets", length = 500)
    private String experienciaPets;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAdocao status = StatusAdocao.PENDENTE;
    
    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    
    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;
    
    @Column(name = "observacoes", length = 500)
    private String observacoes;
    
    public enum StatusAdocao {
        PENDENTE, APROVADO, RECUSADO, CANCELADO
    }

    // Construtores
    public FormularioAdocao() {}

    public FormularioAdocao(Long id, Usuario usuario, Animal animal, String endereco, 
                           Boolean possuiOutrosPets, String tipoResidencia, Boolean temQuintal, 
                           String motivoAdocao, String experienciaPets, StatusAdocao status, 
                           LocalDateTime dataSolicitacao, LocalDateTime dataResposta, String observacoes) {
        this.id = id;
        this.usuario = usuario;
        this.animal = animal;
        this.endereco = endereco;
        this.possuiOutrosPets = possuiOutrosPets;
        this.tipoResidencia = tipoResidencia;
        this.temQuintal = temQuintal;
        this.motivoAdocao = motivoAdocao;
        this.experienciaPets = experienciaPets;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
        this.dataResposta = dataResposta;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public Boolean getPossuiOutrosPets() { return possuiOutrosPets; }
    public void setPossuiOutrosPets(Boolean possuiOutrosPets) { this.possuiOutrosPets = possuiOutrosPets; }

    public String getTipoResidencia() { return tipoResidencia; }
    public void setTipoResidencia(String tipoResidencia) { this.tipoResidencia = tipoResidencia; }

    public Boolean getTemQuintal() { return temQuintal; }
    public void setTemQuintal(Boolean temQuintal) { this.temQuintal = temQuintal; }

    public String getMotivoAdocao() { return motivoAdocao; }
    public void setMotivoAdocao(String motivoAdocao) { this.motivoAdocao = motivoAdocao; }

    public String getExperienciaPets() { return experienciaPets; }
    public void setExperienciaPets(String experienciaPets) { this.experienciaPets = experienciaPets; }

    public StatusAdocao getStatus() { return status; }
    public void setStatus(StatusAdocao status) { this.status = status; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public LocalDateTime getDataResposta() { return dataResposta; }
    public void setDataResposta(LocalDateTime dataResposta) { this.dataResposta = dataResposta; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
