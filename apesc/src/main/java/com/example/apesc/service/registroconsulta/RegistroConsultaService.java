package com.example.apesc.service.registroconsulta;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.model.enums.TipoConsulta;

import java.time.LocalDate;
import java.util.List;

public interface RegistroConsultaService {

    RegistroConsulta save(RegistroConsulta registroConsulta);

    RegistroConsulta findById(Long id);

    void delete(Long id);

    RegistroConsulta update(RegistroConsulta registroConsulta);

    List<RegistroConsulta> search(
            LocalDate dataPesquisa,
            String nomePesquisador,
            String nomeFuncionario,
            TipoConsulta tipoConsulta,
            Boolean semConsulta
    );
}
