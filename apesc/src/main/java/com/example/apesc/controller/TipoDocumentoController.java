package com.example.apesc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apesc.service.documenttype.TipoDocumentoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tipo-documento")
@RequiredArgsConstructor
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;
    
}
