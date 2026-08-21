package com.example.apesc.service.registroconsulta;

import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.specification.RegistroConsultaSearchFilter;

import java.util.List;

public interface RegistroConsultaService {

    RegistroConsulta save(RegistroConsulta registroConsulta);

    RegistroConsulta findById(Long id);

    void delete(Long id);

    RegistroConsulta update(RegistroConsulta registroConsulta);

    List<RegistroConsulta> search(RegistroConsultaSearchFilter filtro);
}
