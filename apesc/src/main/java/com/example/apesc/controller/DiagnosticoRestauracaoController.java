package com.example.apesc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apesc.service.restorationdiagnosis.DiagnosticoRestauracaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diagnostico-restauracao")
@RequiredArgsConstructor
public class DiagnosticoRestauracaoController {
    
    private final DiagnosticoRestauracaoService diagnosticoRestauracaoService;
}
