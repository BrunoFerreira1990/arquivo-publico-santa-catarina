package com.example.apesc.controller;

import com.example.apesc.dto.FuncionarioDTO;
import com.example.apesc.model.Funcionario;
import com.example.apesc.service.employee.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/setor/{setor}")
    public ResponseEntity<List<FuncionarioDTO>> findBySetor(@PathVariable String setor) {
        List<Funcionario> entidades = funcionarioService.findBySetor(setor);
        if (entidades.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<FuncionarioDTO> dtos = entidades.stream()
            .map(FuncionarioDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> findAll() {
        List<Funcionario> entidades = funcionarioService.findAll();
        List<FuncionarioDTO> dtos = entidades.stream()
            .map(FuncionarioDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
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
