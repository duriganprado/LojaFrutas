package com.fruta.model;

import java.math.BigDecimal;
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
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "frutas")
public class Fruta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Column(nullable = false)
    private String categoria; // Maçã, Banana, Morango, etc.
    
    @Column(name = "origem")
    private String origem;
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Column(nullable = false)
    private BigDecimal preco;
    
    @NotNull(message = "Estoque é obrigatório")
    @PositiveOrZero(message = "Estoque não pode ser negativo")
    @Column(nullable = false)
    private Integer estoque;
    
    @Column(name = "unidade_medida")
    private String unidadeMedida; // Kg, Unidade, Dúzia
    
    @Column(name = "cor")
    private String cor;
    
    @Column(name = "sabor")
    private String sabor; // Doce, Ácido, Neutro
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @Column(name = "organico")
    private Boolean organico = false;
    
    @Column(name = "em_promocao")
    private Boolean emPromocao = false;
    
    @Column(name = "disponivel")
    private Boolean disponivel = true;
    
    @Column(name = "foto_url")
    private String fotoUrl;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    @OneToMany(mappedBy = "fruta", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Pedido> pedidos = new HashSet<>();

    // Construtores
    public Fruta() {}

    public Fruta(Long id, String nome, String categoria, String origem, BigDecimal preco, 
                 Integer estoque, String unidadeMedida, String cor, String sabor, String descricao, 
                 Boolean organico, Boolean emPromocao, Boolean disponivel, String fotoUrl, 
                 LocalDateTime dataCadastro, Set<Pedido> pedidos) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.origem = origem;
        this.preco = preco;
        this.estoque = estoque;
        this.unidadeMedida = unidadeMedida;
        this.cor = cor;
        this.sabor = sabor;
        this.descricao = descricao;
        this.organico = organico;
        this.emPromocao = emPromocao;
        this.disponivel = disponivel;
        this.fotoUrl = fotoUrl;
        this.dataCadastro = dataCadastro;
        this.pedidos = pedidos;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getSabor() { return sabor; }
    public void setSabor(String sabor) { this.sabor = sabor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Boolean getOrganico() { return organico; }
    public void setOrganico(Boolean organico) { this.organico = organico; }

    public Boolean getEmPromocao() { return emPromocao; }
    public void setEmPromocao(Boolean emPromocao) { this.emPromocao = emPromocao; }

    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public Set<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(Set<Pedido> pedidos) { this.pedidos = pedidos; }
}
