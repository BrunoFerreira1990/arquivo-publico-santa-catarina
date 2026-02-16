package com.example.apesc.controller;

import com.example.apesc.dto.AcervoDocumentalDTO;
import com.example.apesc.model.AcervoDocumental;
import com.example.apesc.service.AcervoDocumentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-documental")
@RequiredArgsConstructor
public class AcervoDocumentalController {
    
    private final AcervoDocumentalService acervoDocumentalService;

    @PostMapping
    public ResponseEntity<AcervoDocumentalDTO> save(@RequestBody @Validated AcervoDocumentalDTO dto) {
        AcervoDocumental acervoSalvo = acervoDocumentalService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoDocumentalDTO.fromEntity(acervoSalvo));
    }

    @GetMapping
    public ResponseEntity<List<AcervoDocumentalDTO>> listAll() {
        List<AcervoDocumentalDTO> acervos = acervoDocumentalService.findAll().stream()
                .map(AcervoDocumentalDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(acervos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcervoDocumentalDTO> findById(@PathVariable Long id) {
        return acervoDocumentalService.findById(id)
                .map(acervo -> ResponseEntity.ok(AcervoDocumentalDTO.fromEntity(acervo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoDocumentalDTO> update(
            @PathVariable Long id, 
            @RequestBody AcervoDocumentalDTO dto) {
        
        if (!acervoDocumentalService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        AcervoDocumental acervoAtualizado = dto.toEntity();
        acervoAtualizado.setId(id);
        
        AcervoDocumental acervoSalvo = acervoDocumentalService.update(acervoAtualizado);
        return ResponseEntity.ok(AcervoDocumentalDTO.fromEntity(acervoSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!acervoDocumentalService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        acervoDocumentalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo-documento/{tipoDocumentoId}")
    public ResponseEntity<List<AcervoDocumentalDTO>> findByTipoDocumento(
            @PathVariable Long tipoDocumentoId) {
        
        List<AcervoDocumentalDTO> acervos = acervoDocumentalService
                .findByTipoDocumento(tipoDocumentoId).stream()
                .map(AcervoDocumentalDTO::fromEntity)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(acervos);
    }
}
