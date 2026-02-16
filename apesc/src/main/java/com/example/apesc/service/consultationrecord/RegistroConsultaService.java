package com.example.apesc.service.consultationrecord;

import com.example.apesc.model.RegistroConsulta;

public interface RegistroConsultaService {
    
    RegistroConsulta save(RegistroConsulta registroConsulta);
    
    RegistroConsulta findById(Long id);
    
    void delete(Long id);
    
    RegistroConsulta update(RegistroConsulta registroConsulta);
}
