package com.example.apesc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apesc.service.researchers.PesquisadorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pesquisador")
@RequiredArgsConstructor
public class PesquisadorController {
    
    private final PesquisadorService pesquisadorService;
    
}
