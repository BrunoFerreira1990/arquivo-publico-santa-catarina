package com.example.apesc.controller;

import com.example.apesc.dto.AcervoIconograficoPersonalidadesDTO;
import com.example.apesc.model.AcervoIconograficoPersonalidades;
import com.example.apesc.service.acervoiconograficopersonalidades.AcervoIconograficoPersonalidadesService;
import com.example.apesc.specification.AcervoIconograficoPersonalidadesSearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-iconografico/personalidades")
@RequiredArgsConstructor
public class AcervoIconograficoPersonalidadesController {

    private final AcervoIconograficoPersonalidadesService personalidadesService;

    @PostMapping
    public ResponseEntity<AcervoIconograficoPersonalidadesDTO> save(@RequestBody AcervoIconograficoPersonalidadesDTO dto) {
        AcervoIconograficoPersonalidades salvo = personalidadesService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoIconograficoPersonalidadesDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<AcervoIconograficoPersonalidadesDTO>> listAll() {
        List<AcervoIconograficoPersonalidadesDTO> personalidades = personalidadesService.findAll().stream()
                .map(AcervoIconograficoPersonalidadesDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(personalidades);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AcervoIconograficoPersonalidadesDTO>> search(@ModelAttribute AcervoIconograficoPersonalidadesSearchFilter filtro) {
        List<AcervoIconograficoPersonalidadesDTO> personalidades = personalidadesService.search(filtro).stream()
                .map(AcervoIconograficoPersonalidadesDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(personalidades);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoIconograficoPersonalidadesDTO> update(@PathVariable Long id, @RequestBody AcervoIconograficoPersonalidadesDTO dto) {
        AcervoIconograficoPersonalidades atualizado = dto.toEntity();
        atualizado.setId(id);
        AcervoIconograficoPersonalidades salvo = personalidadesService.update(atualizado);
        return ResponseEntity.ok(AcervoIconograficoPersonalidadesDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personalidadesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
