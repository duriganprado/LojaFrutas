package com.petshop.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "animais")
public class Animal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "Espécie é obrigatória")
    @Column(nullable = false)
    private String especie; // Cachorro, Gato, Pássaro, etc.
    
    @Column(name = "raca")
    private String raca;
    
    @NotNull(message = "Idade é obrigatória")
    @Positive(message = "Idade deve ser positiva")
    @Column(nullable = false)
    private Integer idade;
    
    @Column(name = "sexo")
    private String sexo; // Macho, Fêmea
    
    @Column(name = "cor")
    private String cor;
    
    @Column(name = "porte")
    private String porte; // Pequeno, Médio, Grande
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @Column(name = "vacinado")
    private Boolean vacinado = false;
    
    @Column(name = "castrado")
    private Boolean castrado = false;
    
    @Column(name = "disponivel_adocao")
    private Boolean disponivelAdocao = true;
    
    @Column(name = "foto_url")
    private String fotoUrl;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<FormularioAdocao> formularios = new HashSet<>();

    // Construtores
    public Animal() {}

    public Animal(Long id, String nome, String especie, String raca, Integer idade, String sexo, 
                  String cor, String porte, String descricao, Boolean vacinado, Boolean castrado, 
                  Boolean disponivelAdocao, String fotoUrl, LocalDateTime dataCadastro, 
                  Set<FormularioAdocao> formularios) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.sexo = sexo;
        this.cor = cor;
        this.porte = porte;
        this.descricao = descricao;
        this.vacinado = vacinado;
        this.castrado = castrado;
        this.disponivelAdocao = disponivelAdocao;
        this.fotoUrl = fotoUrl;
        this.dataCadastro = dataCadastro;
        this.formularios = formularios;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getPorte() { return porte; }
    public void setPorte(String porte) { this.porte = porte; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Boolean getVacinado() { return vacinado; }
    public void setVacinado(Boolean vacinado) { this.vacinado = vacinado; }

    public Boolean getCastrado() { return castrado; }
    public void setCastrado(Boolean castrado) { this.castrado = castrado; }

    public Boolean getDisponivelAdocao() { return disponivelAdocao; }
    public void setDisponivelAdocao(Boolean disponivelAdocao) { this.disponivelAdocao = disponivelAdocao; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public Set<FormularioAdocao> getFormularios() { return formularios; }
    public void setFormularios(Set<FormularioAdocao> formularios) { this.formularios = formularios; }
}

