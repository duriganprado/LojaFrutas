package com.petshop.controller;

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

import com.petshop.dto.FormularioAdocaoDTO;
import com.petshop.model.Animal;
import com.petshop.model.FormularioAdocao;
import com.petshop.model.Usuario;
import com.petshop.service.AnimalService;
import com.petshop.service.FormularioAdocaoService;
import com.petshop.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/formularios")
public class FormularioAdocaoController {
    
    @Autowired
    private FormularioAdocaoService formularioService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private AnimalService animalService;
    
    @GetMapping
    public ResponseEntity<List<FormularioAdocao>> listarTodos() {
        return ResponseEntity.ok(formularioService.listarTodos());
    }
    
    @GetMapping("/meus")
    public ResponseEntity<?> listarMeusFormularios(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            return ResponseEntity.ok(formularioService.listarPorUsuario(usuario));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return formularioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FormularioAdocao>> listarPorStatus(@PathVariable String status) {
        FormularioAdocao.StatusAdocao statusEnum = FormularioAdocao.StatusAdocao.valueOf(status.toUpperCase());
        return ResponseEntity.ok(formularioService.listarPorStatus(statusEnum));
    }
    
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<FormularioAdocao>> listarPorAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(formularioService.listarPorAnimal(animalId));
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody FormularioAdocaoDTO dto, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuário não autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // Buscar o usuário autenticado
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            // Buscar o animal
            Animal animal = animalService.buscarPorId(dto.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
            
            // Criar o formulário
            FormularioAdocao formulario = new FormularioAdocao();
            formulario.setAnimal(animal);
            formulario.setUsuario(usuario);
            formulario.setEndereco(dto.getEndereco());
            formulario.setMotivoAdocao(dto.getMotivoAdocao());
            formulario.setExperienciaPets(dto.getExperienciaAnimais());
            
            FormularioAdocao novoFormulario = formularioService.salvar(formulario);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Formulário enviado com sucesso!");
            response.put("formulario", novoFormulario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String observacoes = body.getOrDefault("observacoes", "");
            FormularioAdocao aprovado = formularioService.aprovar(id, observacoes);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Formulário aprovado com sucesso!");
            response.put("formulario", aprovado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/recusar")
    public ResponseEntity<?> recusar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String observacoes = body.getOrDefault("observacoes", "");
            FormularioAdocao recusado = formularioService.recusar(id, observacoes);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Formulário recusado!");
            response.put("formulario", recusado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            formularioService.cancelar(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Formulário cancelado!");
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
            formularioService.deletar(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Formulário deletado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
