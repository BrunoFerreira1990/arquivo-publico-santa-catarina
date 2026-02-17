package com.example.apesc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apesc.service.consultationrecord.RegistroConsultaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/registro-consulta")
@RequiredArgsConstructor
public class RegistroConsultaController {
    
    private final RegistroConsultaService registroConsultaService;
    
}
