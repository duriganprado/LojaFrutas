package com.fruta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fruta.model.Cliente;
import com.fruta.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCliente(Cliente cliente);
    List<Pedido> findByStatus(Pedido.StatusPedido status);
    List<Pedido> findByFrutaId(Long frutaId);
}
