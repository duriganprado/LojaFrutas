package com.fruta.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PedidoDTO {
    
    @NotNull(message = "ID da fruta é obrigatório")
    private Long frutaId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantidade;
    
    @NotBlank(message = "Endereço de entrega é obrigatório")
    private String enderecoEntrega;
    
    private String telefoneEntrega;
    
    private LocalDateTime dataEntregaDesejada;
    
    private String observacoes;
    
    // Getters and Setters
    public Long getFrutaId() {
        return frutaId;
    }
    
    public void setFrutaId(Long frutaId) {
        this.frutaId = frutaId;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }
    
    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
    
    public String getTelefoneEntrega() {
        return telefoneEntrega;
    }
    
    public void setTelefoneEntrega(String telefoneEntrega) {
        this.telefoneEntrega = telefoneEntrega;
    }
    
    public LocalDateTime getDataEntregaDesejada() {
        return dataEntregaDesejada;
    }
    
    public void setDataEntregaDesejada(LocalDateTime dataEntregaDesejada) {
        this.dataEntregaDesejada = dataEntregaDesejada;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
