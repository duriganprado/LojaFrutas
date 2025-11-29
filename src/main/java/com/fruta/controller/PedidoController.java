package com.fruta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fruta.dto.PedidoDTO;
import com.fruta.model.Cliente;
import com.fruta.model.Fruta;
import com.fruta.model.Pedido;
import com.fruta.service.ClienteService;
import com.fruta.service.FrutaService;
import com.fruta.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private FrutaService frutaService;
    
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
    
    @GetMapping("/meus")
    public ResponseEntity<?> listarMeusPedidos(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Cliente cliente = clienteService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            return ResponseEntity.ok(pedidoService.listarPorCliente(cliente));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> listarPorStatus(@PathVariable String status) {
        Pedido.StatusPedido statusEnum = Pedido.StatusPedido.valueOf(status.toUpperCase());
        return ResponseEntity.ok(pedidoService.listarPorStatus(statusEnum));
    }
    
    @GetMapping("/fruta/{frutaId}")
    public ResponseEntity<List<Pedido>> listarPorFruta(@PathVariable Long frutaId) {
        return ResponseEntity.ok(pedidoService.listarPorFruta(frutaId));
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PedidoDTO dto, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Cliente não autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // Buscar o cliente autenticado
            String email = authentication.getName();
            Cliente cliente = clienteService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            
            // Buscar a fruta
            Fruta fruta = frutaService.buscarPorId(dto.getFrutaId())
                    .orElseThrow(() -> new RuntimeException("Fruta não encontrada"));
            
            // Criar o pedido
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setFruta(fruta);
            pedido.setQuantidade(dto.getQuantidade());
            pedido.setPrecoUnitario(fruta.getPreco());
            pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
            pedido.setTelefoneEntrega(dto.getTelefoneEntrega());
            pedido.setDataEntregaDesejada(dto.getDataEntregaDesejada());
            pedido.setObservacoes(dto.getObservacoes());
            
            Pedido novoPedido = pedidoService.salvar(pedido);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido criado com sucesso!");
            response.put("pedido", novoPedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String observacoes = body.getOrDefault("observacoes", "");
            Pedido confirmado = pedidoService.confirmar(id, observacoes);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido confirmado com sucesso!");
            response.put("pedido", confirmado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/preparando")
    public ResponseEntity<?> marcarComoPreparando(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.marcarComoPreparando(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido marcado como preparando!");
            response.put("pedido", pedido);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/entregue")
    public ResponseEntity<?> marcarComoEntregue(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.marcarComoEntregue(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido marcado como entregue!");
            response.put("pedido", pedido);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String observacoes = body.getOrDefault("observacoes", "");
            Pedido pedido = pedidoService.cancelar(id, observacoes);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido cancelado!");
            response.put("pedido", pedido);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            pedidoService.deletar(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido deletado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
