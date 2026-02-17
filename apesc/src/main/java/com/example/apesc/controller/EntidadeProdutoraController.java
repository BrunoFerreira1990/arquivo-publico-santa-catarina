package com.example.apesc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apesc.service.productionentity.EntidadeProdutoraService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entidade-produtora")
@RequiredArgsConstructor
public class EntidadeProdutoraController {
    
    private final EntidadeProdutoraService entidadeProdutoraService;
}
