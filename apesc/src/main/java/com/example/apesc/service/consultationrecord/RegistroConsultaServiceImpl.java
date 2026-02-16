package com.example.apesc.service.consultationrecord;

import org.springframework.stereotype.Service;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.repository.RegistroConsultaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistroConsultaServiceImpl implements RegistroConsultaService {
    
    private final RegistroConsultaRepository registroConsultaRepository;

    public RegistroConsulta save(RegistroConsulta registroConsulta) {
        return registroConsultaRepository.save(registroConsulta);
    }
    
    public RegistroConsulta findById(Long id) {
        return registroConsultaRepository.findById(id).orElse(null);
    }
    
    public void delete(Long id) {
        registroConsultaRepository.deleteById(id);
    }
    
    public RegistroConsulta update(RegistroConsulta registroConsulta) {
        return registroConsultaRepository.save(registroConsulta);
    }

}
