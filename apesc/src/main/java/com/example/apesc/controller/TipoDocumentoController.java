package com.example.apesc.controller;

import com.example.apesc.model.TipoDocumento;
import com.example.apesc.service.documenttype.TipoDocumentoServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipo-documento")
@RequiredArgsConstructor
public class TipoDocumentoController {

    private final TipoDocumentoServiceImpl tipoDocumentoService;

    @PostMapping
    public ResponseEntity<TipoDocumento> save(TipoDocumento tipoDocumento) {
        tipoDocumentoService.save(tipoDocumento);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoDocumento);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TipoDocumento> update(@PathVariable Long id, @RequestBody TipoDocumento tipoDocumento) {
        tipoDocumento.setId(id);
        return ResponseEntity.ok(tipoDocumentoService.update(tipoDocumento));
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<TipoDocumento> findByName(@PathVariable String name) {
        TipoDocumento tipo = tipoDocumentoService.findByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(tipo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoDocumento> delete(Long id) {
        tipoDocumentoService.delete(id);
        return ResponseEntity.ok().build();
    }

}
