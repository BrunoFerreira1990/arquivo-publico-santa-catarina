package com.example.apesc.controller;

import com.example.apesc.dto.RegistroConsultaDTO;
import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.enums.TipoConsulta;
import com.example.apesc.service.registroconsulta.RegistroConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registro-consulta")
@RequiredArgsConstructor
public class RegistroConsultaController {

    private final RegistroConsultaService registroConsultaService;

    @PostMapping
    public ResponseEntity<RegistroConsultaDTO> save(@RequestBody RegistroConsultaDTO dto) {
        RegistroConsulta salvo = registroConsultaService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(RegistroConsultaDTO.fromEntity(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroConsultaDTO> findById(@PathVariable Long id) {
        RegistroConsulta entidade = registroConsultaService.findById(id);
        if (entidade != null) {
            return ResponseEntity.ok(RegistroConsultaDTO.fromEntity(entidade));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RegistroConsultaDTO> update(@PathVariable Long id, @RequestBody RegistroConsultaDTO dto) {
        RegistroConsulta entity = dto.toEntity();
        entity.setId(id);
        RegistroConsulta atualizado = registroConsultaService.update(entity);
        return ResponseEntity.ok(RegistroConsultaDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        registroConsultaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<RegistroConsultaDTO>> search(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPesquisa,
            @RequestParam(required = false) String nomePesquisador,
            @RequestParam(required = false) String nomeFuncionario,
            @RequestParam(required = false) TipoConsulta tipoConsulta,
            @RequestParam(required = false) Boolean semConsulta) {

        List<RegistroConsultaDTO> registros = registroConsultaService
                .search(dataPesquisa, nomePesquisador, nomeFuncionario, tipoConsulta, semConsulta)
                .stream()
                .map(RegistroConsultaDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(registros);
    }
}
