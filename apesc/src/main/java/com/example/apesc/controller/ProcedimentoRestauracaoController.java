package com.example.apesc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apesc.service.restorationprocedure.ProcedimentoRestauracaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/procedimento-restauracao")
@RequiredArgsConstructor
public class ProcedimentoRestauracaoController {
    
    private final ProcedimentoRestauracaoService procedimentoRestauracaoService;
}
