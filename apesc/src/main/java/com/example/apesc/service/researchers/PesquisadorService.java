package com.example.apesc.service.researchers;

import java.util.List;

import com.example.apesc.model.Pesquisador;

public interface PesquisadorService {

    Pesquisador save(Pesquisador pesquisador);

    List<Pesquisador> findByName(String name);

    void delete(Long id);

    Pesquisador update(Pesquisador pesquisador);
    
}
