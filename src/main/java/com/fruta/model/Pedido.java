package com.fruta.model;

import java.math.BigDecimal;
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
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "Cliente é obrigatório")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "fruta_id", nullable = false)
    @NotNull(message = "Fruta é obrigatória")
    private Fruta fruta;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(nullable = false)
    private Integer quantidade;
    
    @NotNull(message = "Preço unitário é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Column(nullable = false)
    private BigDecimal precoUnitario;
    
    @NotBlank(message = "Endereço de entrega é obrigatório")
    @Column(nullable = false)
    private String enderecoEntrega;
    
    @Column(name = "telefone_entrega")
    private String telefoneEntrega;
    
    @Column(name = "data_entrega_desejada")
    private LocalDateTime dataEntregaDesejada;
    
    @Column(name = "observacoes", length = 500)
    private String observacoes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.PENDENTE;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido = LocalDateTime.now();
    
    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;
    
    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;
    
    public enum StatusPedido {
        PENDENTE, CONFIRMADO, PREPARANDO, ENTREGUE, CANCELADO
    }

    // Construtores
    public Pedido() {}

    public Pedido(Long id, Cliente cliente, Fruta fruta, Integer quantidade, BigDecimal precoUnitario,
                 String enderecoEntrega, String telefoneEntrega, LocalDateTime dataEntregaDesejada,
                 String observacoes, StatusPedido status, LocalDateTime dataPedido, 
                 LocalDateTime dataConfirmacao, LocalDateTime dataEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.fruta = fruta;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.enderecoEntrega = enderecoEntrega;
        this.telefoneEntrega = telefoneEntrega;
        this.dataEntregaDesejada = dataEntregaDesejada;
        this.observacoes = observacoes;
        this.status = status;
        this.dataPedido = dataPedido;
        this.dataConfirmacao = dataConfirmacao;
        this.dataEntrega = dataEntrega;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Fruta getFruta() { return fruta; }
    public void setFruta(Fruta fruta) { this.fruta = fruta; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public String getTelefoneEntrega() { return telefoneEntrega; }
    public void setTelefoneEntrega(String telefoneEntrega) { this.telefoneEntrega = telefoneEntrega; }

    public LocalDateTime getDataEntregaDesejada() { return dataEntregaDesejada; }
    public void setDataEntregaDesejada(LocalDateTime dataEntregaDesejada) { this.dataEntregaDesejada = dataEntregaDesejada; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public LocalDateTime getDataConfirmacao() { return dataConfirmacao; }
    public void setDataConfirmacao(LocalDateTime dataConfirmacao) { this.dataConfirmacao = dataConfirmacao; }

    public LocalDateTime getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDateTime dataEntrega) { this.dataEntrega = dataEntrega; }
}
