package com.fruta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruta.model.Cliente;
import com.fruta.model.Pedido;
import com.fruta.repository.PedidoRepository;

@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private FrutaService frutaService;
    
    public Pedido salvar(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setDataPedido(LocalDateTime.now());
            pedido.setStatus(Pedido.StatusPedido.PENDENTE);
        }
        return pedidoRepository.save(pedido);
    }
    
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
    
    public List<Pedido> listarPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }
    
    public List<Pedido> listarPorStatus(Pedido.StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }
    
    public List<Pedido> listarPorFruta(Long frutaId) {
        return pedidoRepository.findByFrutaId(frutaId);
    }
    
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
    
    public Pedido confirmar(Long id, String observacoes) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        pedido.setStatus(Pedido.StatusPedido.CONFIRMADO);
        pedido.setDataConfirmacao(LocalDateTime.now());
        
        if (observacoes != null) {
            pedido.setObservacoes(observacoes);
        }
        
        // Diminuir estoque
        frutaService.diminuirEstoque(pedido.getFruta().getId(), pedido.getQuantidade());
        
        return pedidoRepository.save(pedido);
    }
    
    public Pedido marcarComoPreparando(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        pedido.setStatus(Pedido.StatusPedido.PREPARANDO);
        
        return pedidoRepository.save(pedido);
    }
    
    public Pedido marcarComoEntregue(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        pedido.setStatus(Pedido.StatusPedido.ENTREGUE);
        pedido.setDataEntrega(LocalDateTime.now());
        
        return pedidoRepository.save(pedido);
    }
    
    public Pedido cancelar(Long id, String observacoes) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        // Se já foi confirmado, devolvemos estoque
        if (pedido.getStatus() == Pedido.StatusPedido.CONFIRMADO || 
            pedido.getStatus() == Pedido.StatusPedido.PREPARANDO) {
            frutaService.salvar(pedido.getFruta());
            pedido.getFruta().setEstoque(pedido.getFruta().getEstoque() + pedido.getQuantidade());
        }
        
        pedido.setStatus(Pedido.StatusPedido.CANCELADO);
        pedido.setObservacoes(observacoes);
        
        return pedidoRepository.save(pedido);
    }
    
    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
}
