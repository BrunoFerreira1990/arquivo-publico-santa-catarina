package com.example.apesc.controller;

import com.example.apesc.dto.AcervoIconograficoDTO;
import com.example.apesc.model.AcervoIconografico;
import com.example.apesc.service.acervoiconografico.AcervoIconograficoService;
import com.example.apesc.specification.AcervoIconograficoSearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-iconografico")
@RequiredArgsConstructor
public class AcervoIconograficoController {

    private final AcervoIconograficoService acervoIconograficoService;

    @PostMapping
    public ResponseEntity<AcervoIconograficoDTO> save(@RequestBody AcervoIconograficoDTO dto) {
        AcervoIconografico salvo = acervoIconograficoService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoIconograficoDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<AcervoIconograficoDTO>> listAll() {
        List<AcervoIconograficoDTO> acervos = acervoIconograficoService.findAllWithRelations().stream()
                .map(AcervoIconograficoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(acervos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AcervoIconograficoDTO>> search(@ModelAttribute AcervoIconograficoSearchFilter filtro) {
        List<AcervoIconograficoDTO> acervos = acervoIconograficoService.search(filtro).stream()
                .map(AcervoIconograficoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(acervos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoIconograficoDTO> update(@PathVariable Long id, @RequestBody AcervoIconograficoDTO dto) {
        AcervoIconografico atualizado = dto.toEntity();
        atualizado.setId(id);
        AcervoIconografico salvo = acervoIconograficoService.update(atualizado);
        return ResponseEntity.ok(AcervoIconograficoDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        acervoIconograficoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo-documento/{tipoDocumentoId}")
    public ResponseEntity<List<AcervoIconograficoDTO>> findByTipoDocumento(@PathVariable Long tipoDocumentoId) {
        List<AcervoIconograficoDTO> acervos = acervoIconograficoService.findByTipoDocumento(tipoDocumentoId).stream()
                .map(AcervoIconograficoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(acervos);
    }
}
