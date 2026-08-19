package com.example.apesc.controller;

import com.example.apesc.dto.AcervoCartograficoDTO;
import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.service.acervocartografico.AcervoCartograficoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-cartografico")
@RequiredArgsConstructor
public class AcervoCartograficoController {

    private final AcervoCartograficoService acervoCartograficoService;

    @PostMapping
    public ResponseEntity<AcervoCartograficoDTO> save(@RequestBody AcervoCartograficoDTO dto) {
        AcervoCartografico salvo = acervoCartograficoService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoCartograficoDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<AcervoCartograficoDTO>> listAll() {
        List<AcervoCartograficoDTO> acervos = acervoCartograficoService.findAllWithRelations().stream()
                .map(AcervoCartograficoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(acervos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcervoCartograficoDTO> findById(@PathVariable Long id) {
        return acervoCartograficoService.findByIdWithRelations(id)
                .map(acervo -> ResponseEntity.ok(AcervoCartograficoDTO.fromEntity(acervo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoCartograficoDTO> update(
            @PathVariable Long id,
            @RequestBody AcervoCartograficoDTO dto) {

        AcervoCartografico acervoAtualizado = dto.toEntity();
        acervoAtualizado.setId(id);

        AcervoCartografico acervoSalvo = acervoCartograficoService.update(acervoAtualizado);
        return ResponseEntity.ok(AcervoCartograficoDTO.fromEntity(acervoSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        acervoCartograficoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo-documento/{tipoDocumentoId}")
    public ResponseEntity<List<AcervoCartograficoDTO>> findByTipoDocumento(
            @PathVariable Long tipoDocumentoId) {

        List<AcervoCartograficoDTO> acervos = acervoCartograficoService
                .findByTipoDocumento(tipoDocumentoId).stream()
                .map(AcervoCartograficoDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(acervos);
    }
}
