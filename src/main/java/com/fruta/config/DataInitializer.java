package com.fruta.config;

import com.fruta.model.Fruta;
import com.fruta.model.Cliente;
import com.fruta.repository.FrutaRepository;
import com.fruta.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private FrutaRepository frutaRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if database is empty
        if (clienteRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        // Criar usuário admin
        Cliente admin = new Cliente();
        admin.setNome("Administrador");
        admin.setEmail("admin@frutashop.com");
        admin.setSenha(passwordEncoder.encode("admin123"));
        admin.setTelefone("(11) 99999-9999");
        admin.setCpf("12345678900");
        admin.setAtivo(true);
        admin.setDataCadastro(LocalDateTime.now());
        
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("ADMIN");
        adminRoles.add("USER");
        admin.setRoles(adminRoles);
        
        clienteRepository.save(admin);
        
        // Criar cliente de teste
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Santos");
        cliente.setEmail("maria@email.com");
        cliente.setSenha(passwordEncoder.encode("123456"));
        cliente.setTelefone("(11) 98888-8888");
        cliente.setCpf("98765432100");
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        
        Set<String> clienteRoles = new HashSet<>();
        clienteRoles.add("USER");
        cliente.setRoles(clienteRoles);
        
        clienteRepository.save(cliente);
        
        // Criar frutas de exemplo
        createSampleFrutas();
        
        System.out.println("==============================================");
        System.out.println("✓ Dados iniciais da Loja de Frutas criados!");
        System.out.println("==============================================");
        System.out.println("Usuário Admin:");
        System.out.println("  Email: admin@frutashop.com");
        System.out.println("  Senha: admin123");
        System.out.println("----------------------------------------------");
        System.out.println("Cliente Teste:");
        System.out.println("  Email: maria@email.com");
        System.out.println("  Senha: 123456");
        System.out.println("==============================================");
    }
    
    private void createSampleFrutas() {
        // Fruta 1 - Maçã
        Fruta maca = new Fruta();
        maca.setNome("Maçã Fuji");
        maca.setCategoria("Maçã");
        maca.setOrigem("Brasil");
        maca.setPreco(BigDecimal.valueOf(5.50));
        maca.setEstoque(100);
        maca.setUnidadeMedida("Kg");
        maca.setCor("Vermelha");
        maca.setSabor("Doce");
        maca.setDescricao("Maçã Fuji suculenta e doce, perfeita para comer fresca ou em receitas.");
        maca.setOrganico(true);
        maca.setEmPromocao(false);
        maca.setDisponivel(true);
        maca.setDataCadastro(LocalDateTime.now());
        frutaRepository.save(maca);
        
        // Fruta 2 - Banana
        Fruta banana = new Fruta();
        banana.setNome("Banana Nanica");
        banana.setCategoria("Banana");
        banana.setOrigem("Brasil");
        banana.setPreco(BigDecimal.valueOf(3.00));
        banana.setEstoque(150);
        banana.setUnidadeMedida("Dúzia");
        banana.setCor("Amarela");
        banana.setSabor("Doce");
        banana.setDescricao("Banana Nanica madura e deliciosa, rica em potássio.");
        banana.setOrganico(false);
        banana.setEmPromocao(true);
        banana.setDisponivel(true);
        banana.setDataCadastro(LocalDateTime.now());
        frutaRepository.save(banana);
        
        // Fruta 3 - Laranja
        Fruta laranja = new Fruta();
        laranja.setNome("Laranja Pera");
        laranja.setCategoria("Cítrica");
        laranja.setOrigem("Brasil");
        laranja.setPreco(BigDecimal.valueOf(4.00));
        laranja.setEstoque(80);
        laranja.setUnidadeMedida("Kg");
        laranja.setCor("Laranja");
        laranja.setSabor("Ácido");
        laranja.setDescricao("Laranja Pera suculenta, perfeita para suco natural.");
        laranja.setOrganico(true);
        laranja.setEmPromocao(false);
        laranja.setDisponivel(true);
        laranja.setDataCadastro(LocalDateTime.now());
        frutaRepository.save(laranja);
        
        // Fruta 4 - Morango
        Fruta morango = new Fruta();
        morango.setNome("Morango Fresco");
        morango.setCategoria("Morango");
        morango.setOrigem("Brasil");
        morango.setPreco(BigDecimal.valueOf(12.00));
        morango.setEstoque(50);
        morango.setUnidadeMedida("Kg");
        morango.setCor("Vermelho");
        morango.setSabor("Doce");
        morango.setDescricao("Morango fresco e saboroso, perfeito para sobremesas.");
        morango.setOrganico(true);
        morango.setEmPromocao(false);
        morango.setDisponivel(true);
        morango.setDataCadastro(LocalDateTime.now());
        frutaRepository.save(morango);
        
        // Fruta 5 - Melancia
        Fruta melancia = new Fruta();
        melancia.setNome("Melancia Vermelha");
        melancia.setCategoria("Melancia");
        melancia.setOrigem("Brasil");
        melancia.setPreco(BigDecimal.valueOf(8.00));
        melancia.setEstoque(40);
        melancia.setUnidadeMedida("Unidade");
        melancia.setCor("Verde/Vermelha");
        melancia.setSabor("Doce");
        melancia.setDescricao("Melancia refrescante, ideal para dias quentes.");
        melancia.setOrganico(false);
        melancia.setEmPromocao(true);
        melancia.setDisponivel(true);
        melancia.setDataCadastro(LocalDateTime.now());
        frutaRepository.save(melancia);
        
        // Fruta 6 - Uva
        Fruta uva = new Fruta();
        uva.setNome("Uva Tinta Importada");
        uva.setCategoria("Uva");
        uva.setOrigem("Importada");
        uva.setPreco(BigDecimal.valueOf(15.00));
        uva.setEstoque(30);
        uva.setUnidadeMedida("Kg");
        uva.setCor("Roxo");
        uva.setSabor("Doce");
        uva.setDescricao("Uva Tinta de qualidade premium, adocicada e suculenta.");
        uva.setOrganico(true);
        uva.setEmPromocao(false);
        uva.setDisponivel(true);
        uva.setDataCadastro(LocalDateTime.now());
        frutaRepository.save(uva);
    }
}
