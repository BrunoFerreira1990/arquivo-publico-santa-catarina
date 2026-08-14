package com.example.apesc.service.researchers;

import java.util.List;

import com.example.apesc.model.Pesquisador;

public interface PesquisadorService {

    Pesquisador save(Pesquisador pesquisador);

    Pesquisador findById(Long id);

    List<Pesquisador> findByNome(String nome);

    List<Pesquisador> search(String nome, String cpf);

    void delete(Long id);

    Pesquisador update(Pesquisador pesquisador);

}
