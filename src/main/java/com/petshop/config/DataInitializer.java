package com.petshop.config;

import com.petshop.model.Animal;
import com.petshop.model.Usuario;
import com.petshop.repository.AnimalRepository;
import com.petshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AnimalRepository animalRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if database is empty
        if (usuarioRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        // Create admin user
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@petshop.com");
        admin.setSenha(passwordEncoder.encode("admin123"));
        admin.setTelefone("(11) 99999-9999");
        admin.setAtivo(true);
        admin.setDataCadastro(LocalDateTime.now());
        
        Set<String> adminRoles = new HashSet<>();
        adminRoles.add("ADMIN");
        adminRoles.add("USER");
        admin.setRoles(adminRoles);
        
        usuarioRepository.save(admin);
        
        // Create test user
        Usuario user = new Usuario();
        user.setNome("João Silva");
        user.setEmail("joao@email.com");
        user.setSenha(passwordEncoder.encode("123456"));
        user.setTelefone("(11) 98888-8888");
        user.setAtivo(true);
        user.setDataCadastro(LocalDateTime.now());
        
        Set<String> userRoles = new HashSet<>();
        userRoles.add("USER");
        user.setRoles(userRoles);
        
        usuarioRepository.save(user);
        
        // Create sample animals
        createSampleAnimals();
        
        System.out.println("==============================================");
        System.out.println("✓ Dados iniciais criados com sucesso!");
        System.out.println("==============================================");
        System.out.println("Usuário Admin:");
        System.out.println("  Email: admin@petshop.com");
        System.out.println("  Senha: admin123");
        System.out.println("----------------------------------------------");
        System.out.println("Usuário Teste:");
        System.out.println("  Email: joao@email.com");
        System.out.println("  Senha: 123456");
        System.out.println("==============================================");
    }
    
    private void createSampleAnimals() {
        // Animal 1
        Animal rex = new Animal();
        rex.setNome("Rex");
        rex.setEspecie("Cachorro");
        rex.setRaca("Labrador");
        rex.setIdade(3);
        rex.setSexo("Macho");
        rex.setCor("Amarelo");
        rex.setPorte("Grande");
        rex.setDescricao("Rex é um cachorro muito brincalhão e carinhoso. Adora crianças e é muito obediente.");
        rex.setVacinado(true);
        rex.setCastrado(true);
        rex.setDisponivelAdocao(true);
        rex.setDataCadastro(LocalDateTime.now());
        animalRepository.save(rex);
        
        // Animal 2
        Animal mia = new Animal();
        mia.setNome("Mia");
        mia.setEspecie("Gato");
        mia.setRaca("Persa");
        mia.setIdade(2);
        mia.setSexo("Fêmea");
        mia.setCor("Branco");
        mia.setPorte("Pequeno");
        mia.setDescricao("Mia é uma gatinha tranquila e elegante. Gosta de carinho e é muito independente.");
        mia.setVacinado(true);
        mia.setCastrado(true);
        mia.setDisponivelAdocao(true);
        mia.setDataCadastro(LocalDateTime.now());
        animalRepository.save(mia);
        
        // Animal 3
        Animal bob = new Animal();
        bob.setNome("Bob");
        bob.setEspecie("Cachorro");
        bob.setRaca("Vira-lata");
        bob.setIdade(1);
        bob.setSexo("Macho");
        bob.setCor("Preto e Branco");
        bob.setPorte("Médio");
        bob.setDescricao("Bob é jovem e cheio de energia. Precisa de espaço para brincar e correr.");
        bob.setVacinado(true);
        bob.setCastrado(false);
        bob.setDisponivelAdocao(true);
        bob.setDataCadastro(LocalDateTime.now());
        animalRepository.save(bob);
        
        // Animal 4
        Animal nina = new Animal();
        nina.setNome("Nina");
        nina.setEspecie("Gato");
        nina.setRaca("Siamês");
        nina.setIdade(4);
        nina.setSexo("Fêmea");
        nina.setCor("Bege");
        nina.setPorte("Pequeno");
        nina.setDescricao("Nina é calma e gosta de ambientes tranquilos. Perfeita para apartamentos.");
        nina.setVacinado(true);
        nina.setCastrado(true);
        nina.setDisponivelAdocao(true);
        nina.setDataCadastro(LocalDateTime.now());
        animalRepository.save(nina);
        
        // Animal 5
        Animal thor = new Animal();
        thor.setNome("Thor");
        thor.setEspecie("Cachorro");
        thor.setRaca("Pastor Alemão");
        thor.setIdade(5);
        thor.setSexo("Macho");
        thor.setCor("Preto e Marrom");
        thor.setPorte("Grande");
        thor.setDescricao("Thor é um excelente guardião e muito leal. Precisa de um tutor experiente.");
        thor.setVacinado(true);
        thor.setCastrado(true);
        thor.setDisponivelAdocao(true);
        thor.setDataCadastro(LocalDateTime.now());
        animalRepository.save(thor);
        
        // Animal 6
        Animal piu = new Animal();
        piu.setNome("Piu");
        piu.setEspecie("Pássaro");
        piu.setRaca("Canário");
        piu.setIdade(1);
        piu.setSexo("Macho");
        piu.setCor("Amarelo");
        piu.setPorte("Pequeno");
        piu.setDescricao("Piu é um canário alegre que adora cantar. Ideal para quem gosta de pássaros.");
        piu.setVacinado(false);
        piu.setCastrado(false);
        piu.setDisponivelAdocao(true);
        piu.setDataCadastro(LocalDateTime.now());
        animalRepository.save(piu);
    }
}
