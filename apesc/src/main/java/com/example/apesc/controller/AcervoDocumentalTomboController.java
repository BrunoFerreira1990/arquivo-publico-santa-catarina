package com.example.apesc.controller;

import com.example.apesc.dto.AcervoDocumentalTomboDTO;
import com.example.apesc.model.AcervoDocumentalTombo;
import com.example.apesc.service.acervodocumentaltombo.AcervoDocumentalTomboService;
import com.example.apesc.specification.AcervoDocumentalTomboSearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-documental/tombo")
@RequiredArgsConstructor
public class AcervoDocumentalTomboController {

    private final AcervoDocumentalTomboService tomboService;

    @PostMapping
    public ResponseEntity<AcervoDocumentalTomboDTO> save(@RequestBody AcervoDocumentalTomboDTO dto) {
        AcervoDocumentalTombo salvo = tomboService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoDocumentalTomboDTO.fromEntity(salvo));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AcervoDocumentalTomboDTO>> search(@ModelAttribute AcervoDocumentalTomboSearchFilter filtro) {
        List<AcervoDocumentalTomboDTO> tombos = tomboService.search(filtro).stream()
                .map(AcervoDocumentalTomboDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tombos);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoDocumentalTomboDTO> update(@PathVariable Long id, @RequestBody AcervoDocumentalTomboDTO dto) {
        AcervoDocumentalTombo atualizado = dto.toEntity();
        atualizado.setId(id);
        AcervoDocumentalTombo salvo = tomboService.update(atualizado);
        return ResponseEntity.ok(AcervoDocumentalTomboDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tomboService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-acervo-documental/{acervoDocumentalId}")
    public ResponseEntity<List<AcervoDocumentalTomboDTO>> findByAcervoDocumento(@PathVariable Long acervoDocumentalId) {
        List<AcervoDocumentalTomboDTO> tombos = tomboService.findByAcervoDocumento(acervoDocumentalId).stream()
                .map(AcervoDocumentalTomboDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tombos);
    }
}
