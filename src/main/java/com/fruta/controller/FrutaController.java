package com.fruta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fruta.model.Fruta;
import com.fruta.service.FileStorageService;
import com.fruta.service.FrutaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/frutas")
public class FrutaController {
    
    @Autowired
    private FrutaService frutaService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @GetMapping
    public ResponseEntity<List<Fruta>> listarTodas() {
        return ResponseEntity.ok(frutaService.listarTodas());
    }
    
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Fruta>> listarDisponiveis() {
        return ResponseEntity.ok(frutaService.listarDisponiveis());
    }
    
    @GetMapping("/organicas")
    public ResponseEntity<List<Fruta>> listarOrganicas() {
        return ResponseEntity.ok(frutaService.listarOrganicas());
    }
    
    @GetMapping("/promocoes")
    public ResponseEntity<List<Fruta>> listarEmPromocao() {
        return ResponseEntity.ok(frutaService.listarEmPromocao());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return frutaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Fruta>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(frutaService.buscarPorCategoria(categoria));
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Fruta>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(frutaService.buscarPorNome(nome));
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Fruta fruta) {
        try {
            Fruta novaFruta = frutaService.salvar(fruta);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Fruta cadastrada com sucesso!");
            response.put("fruta", novaFruta);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Fruta fruta) {
        try {
            fruta.setId(id);
            Fruta atualizada = frutaService.atualizar(fruta);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Fruta atualizada com sucesso!");
            response.put("fruta", atualizada);
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
            frutaService.deletar(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Fruta deletada com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/marcar-indisponivel")
    public ResponseEntity<?> marcarComoIndisponivel(@PathVariable Long id) {
        try {
            frutaService.marcarComoIndisponivel(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Fruta marcada como indisponível!");
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
            frutaService.marcarComoDisponivel(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Fruta marcada como disponível!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/atualizar-estoque")
    public ResponseEntity<?> atualizarEstoque(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        try {
            Integer novaQuantidade = body.get("estoque");
            frutaService.atualizarEstoque(id, novaQuantidade);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Estoque atualizado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/{id}/upload-imagem")
    public ResponseEntity<?> uploadImagem(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Fruta fruta = frutaService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Fruta não encontrada"));
            
            // Deletar imagem antiga se existir
            if (fruta.getFotoUrl() != null && !fruta.getFotoUrl().isEmpty()) {
                String oldFileName = fruta.getFotoUrl().substring(fruta.getFotoUrl().lastIndexOf("/") + 1);
                fileStorageService.deleteFile(oldFileName);
            }
            
            // Armazenar nova imagem
            String fileUrl = fileStorageService.storeFile(file);
            fruta.setFotoUrl(fileUrl);
            frutaService.atualizar(fruta);
            
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
