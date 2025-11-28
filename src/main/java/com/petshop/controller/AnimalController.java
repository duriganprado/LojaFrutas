package com.petshop.controller;

import com.petshop.model.Animal;
import com.petshop.service.AnimalService;
import com.petshop.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {
    
    @Autowired
    private AnimalService animalService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @GetMapping
    public ResponseEntity<List<Animal>> listarTodos() {
        return ResponseEntity.ok(animalService.listarTodos());
    }
    
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Animal>> listarDisponiveis() {
        return ResponseEntity.ok(animalService.listarDisponiveis());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return animalService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Animal>> buscarPorEspecie(@PathVariable String especie) {
        return ResponseEntity.ok(animalService.buscarPorEspecie(especie));
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Animal>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(animalService.buscarPorNome(nome));
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Animal animal) {
        try {
            Animal novoAnimal = animalService.salvar(animal);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Animal cadastrado com sucesso!");
            response.put("animal", novoAnimal);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Animal animal) {
        try {
            animal.setId(id);
            Animal atualizado = animalService.atualizar(animal);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Animal atualizado com sucesso!");
            response.put("animal", atualizado);
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
            animalService.deletar(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Animal deletado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/marcar-adotado")
    public ResponseEntity<?> marcarComoAdotado(@PathVariable Long id) {
        try {
            animalService.marcarComoAdotado(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Animal marcado como adotado!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/marcar-disponivel")
    public ResponseEntity<?> marcarComoDisponivel(@PathVariable Long id) {
        try {
            animalService.marcarComoDisponivel(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Animal marcado como disponível!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Animal animal = animalService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
            
            // Delete old image if exists
            if (animal.getFotoUrl() != null && !animal.getFotoUrl().isEmpty()) {
                String oldFileName = animal.getFotoUrl().substring(animal.getFotoUrl().lastIndexOf("/") + 1);
                fileStorageService.deleteFile(oldFileName);
            }
            
            // Store new image
            String fileUrl = fileStorageService.storeFile(file);
            animal.setFotoUrl(fileUrl);
            animalService.atualizar(animal);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imagem enviada com sucesso!");
            response.put("fileUrl", fileUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
