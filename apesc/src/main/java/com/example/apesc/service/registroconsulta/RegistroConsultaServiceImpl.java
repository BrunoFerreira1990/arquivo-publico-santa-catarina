package com.example.apesc.service.registroconsulta;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.model.RegistroConsulta;
import com.example.apesc.repository.RegistroConsultaRepository;
import com.example.apesc.specification.RegistroConsultaSearchFilter;
import com.example.apesc.specification.RegistroConsultaSpecification;
import com.example.apesc.util.RegistroConsultaValidation;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistroConsultaServiceImpl implements RegistroConsultaService {

    private final RegistroConsultaRepository registroConsultaRepository;
    private final RegistroConsultaValidation registroConsultaValidation;

    public RegistroConsulta save(RegistroConsulta registroConsulta) {
        registroConsultaValidation.validateSave(registroConsulta);
        // dataAtualizacao só existe a partir de uma edição de fato.
        registroConsulta.setDataAtualizacao(null);
        return registroConsultaRepository.save(registroConsulta);
    }

    @Transactional(readOnly = true)
    public RegistroConsulta findById(Long id) {
        return registroConsultaRepository.findByIdWithRelations(id).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        registroConsultaValidation.validateDelete(id);
        registroConsultaRepository.deleteById(id);
    }

    @Transactional
    public RegistroConsulta update(RegistroConsulta registroConsulta) {
        registroConsultaValidation.validateUpdate(registroConsulta);

        RegistroConsulta existente = registroConsultaRepository.findById(registroConsulta.getId())
                .orElseThrow(() -> new CustomException(ErrorConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND));

        // dataRegistro marca quando a consulta foi cadastrada e não deve mudar
        // em edições posteriores (ela pode ser bem diferente de dataPesquisa).
        registroConsulta.setDataRegistro(existente.getDataRegistro());

        // dataAtualizacao só é preenchida aqui, no momento real do PATCH/update.
        registroConsulta.setDataAtualizacao(LocalDateTime.now());

        return registroConsultaRepository.save(registroConsulta);
    }

    @Transactional(readOnly = true)
    public List<RegistroConsulta> search(RegistroConsultaSearchFilter filtro) {
        registroConsultaValidation.validateSearch(filtro);
        Specification<RegistroConsulta> spec = RegistroConsultaSpecification.searchByFields(filtro);
        return registroConsultaRepository.findAll(spec);
    }

}
