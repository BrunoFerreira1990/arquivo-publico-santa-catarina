package com.example.apesc.service.researchers;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.apesc.model.Pesquisador;
import com.example.apesc.repository.PesquisadorRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PesquisadorServiceImpl implements PesquisadorService {

    private final PesquisadorRepository pesquisadorRepository;

    public Pesquisador save(Pesquisador pesquisador) {
        return pesquisadorRepository.save(pesquisador);
    }
    
    public List<Pesquisador> findByName(String name) {
        return pesquisadorRepository.findByName(name);
    }

    public void delete(Long id) {
        pesquisadorRepository.deleteById(id);
    }

    public Pesquisador update(Pesquisador pesquisador) {
        return pesquisadorRepository.save(pesquisador);
    }

}
