package com.example.apesc.service.researchers;


import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apesc.model.Pesquisador;
import com.example.apesc.repository.PesquisadorRepository;
import com.example.apesc.specification.PesquisadorSpecification;
import com.example.apesc.util.PesquisadorValidation;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PesquisadorServiceImpl implements PesquisadorService {

    private final PesquisadorRepository pesquisadorRepository;
    private final PesquisadorValidation pesquisadorValidation;

    @Transactional
    public Pesquisador save(Pesquisador pesquisador) {
        pesquisadorValidation.validateSave(pesquisador);
        return pesquisadorRepository.save(pesquisador);
    }

    @Transactional(readOnly = true)
    public Pesquisador findById(Long id) {
        return pesquisadorRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Pesquisador> findByNome(String nome) {
        return pesquisadorRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Pesquisador> search(String nome, String cpf) {
        Specification<Pesquisador> spec = PesquisadorSpecification.searchByFields(nome, cpf);
        return pesquisadorRepository.findAll(spec);
    }

    @Transactional
    public void delete(Long id) {
        pesquisadorRepository.deleteById(id);
    }

    @Transactional
    public Pesquisador update(Pesquisador pesquisador) {
        pesquisadorValidation.validateUpdate(pesquisador);
        return pesquisadorRepository.save(pesquisador);
    }

}
