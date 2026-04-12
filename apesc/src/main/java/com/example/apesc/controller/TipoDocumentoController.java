package com.example.apesc.controller;

import com.example.apesc.dto.TipoDocumentoDTO;
import com.example.apesc.model.TipoDocumento;
import com.example.apesc.service.documenttype.TipoDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipo-documento")
@RequiredArgsConstructor
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    @PostMapping
    public ResponseEntity<TipoDocumentoDTO> save(@RequestBody TipoDocumentoDTO dto) {
        TipoDocumento tipoDocumentoSalvo = tipoDocumentoService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(TipoDocumentoDTO.fromEntity(tipoDocumentoSalvo));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> update(@PathVariable Long id, @RequestBody TipoDocumentoDTO dto) {
        TipoDocumento entity = dto.toEntity();
        entity.setId(id);
        TipoDocumento atualizado = tipoDocumentoService.update(entity);
        return ResponseEntity.ok(TipoDocumentoDTO.fromEntity(atualizado));
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<TipoDocumentoDTO> findByName(@PathVariable String name) {
        TipoDocumento tipo = tipoDocumentoService.findByNomeDocumento(name);
        return ResponseEntity.status(HttpStatus.OK).body(TipoDocumentoDTO.fromEntity(tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoDocumentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
