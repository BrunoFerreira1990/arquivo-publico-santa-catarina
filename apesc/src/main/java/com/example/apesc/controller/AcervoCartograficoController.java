package com.example.apesc.controller;

import com.example.apesc.dto.AcervoCartograficoDTO;
import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.service.acervocartografico.AcervoCartograficoService;
import com.example.apesc.specification.AcervoCartograficoSearchFilter;
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

    @GetMapping("/search")
    public ResponseEntity<List<AcervoCartograficoDTO>> search(@ModelAttribute AcervoCartograficoSearchFilter filtro) {

        List<AcervoCartograficoDTO> acervos = acervoCartograficoService
                .search(filtro)
                .stream()
                .map(AcervoCartograficoDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(acervos);
    }
}
