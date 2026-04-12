package com.example.apesc.controller;

import com.example.apesc.dto.FuncionarioDTO;
import com.example.apesc.model.Funcionario;
import com.example.apesc.service.employee.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/funcionario")
@RequiredArgsConstructor
public class FuncionarioController {
    
    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<FuncionarioDTO> save(@RequestBody FuncionarioDTO dto) {
        Funcionario salvo = funcionarioService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(FuncionarioDTO.fromEntity(salvo));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<FuncionarioDTO> findByName(@PathVariable String nome) {
        Funcionario entidade = funcionarioService.findByNome(nome);
        if (entidade != null) {
            return ResponseEntity.ok(FuncionarioDTO.fromEntity(entidade));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> update(@PathVariable Long id, @RequestBody FuncionarioDTO dto) {
        Funcionario entity = dto.toEntity();
        entity.setId(id);
        Funcionario atualizado = funcionarioService.update(entity);
        return ResponseEntity.ok(FuncionarioDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
