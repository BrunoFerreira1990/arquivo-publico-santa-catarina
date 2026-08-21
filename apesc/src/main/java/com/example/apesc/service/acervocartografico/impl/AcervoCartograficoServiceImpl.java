package com.example.apesc.service.acervocartografico.impl;

import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.repository.AcervoCartograficoRepository;
import com.example.apesc.repository.EntidadeProdutoraRepository;
import com.example.apesc.repository.TipoDocumentoRepository;
import com.example.apesc.service.acervocartografico.AcervoCartograficoService;
import com.example.apesc.specification.AcervoCartograficoSearchFilter;
import com.example.apesc.specification.AcervoCartograficoSpecification;
import com.example.apesc.util.AcervoCartograficoValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AcervoCartograficoServiceImpl implements AcervoCartograficoService {

    private final AcervoCartograficoRepository acervoCartograficoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EntidadeProdutoraRepository entidadeProdutoraRepository;
    private final AcervoCartograficoValidation acervoCartograficoValidation;

    @Transactional
    public AcervoCartografico save(AcervoCartografico acervoCartografico) {
        acervoCartograficoValidation.validateSave(acervoCartografico, acervoCartograficoRepository, tipoDocumentoRepository, entidadeProdutoraRepository);

        rehydrateRelationships(acervoCartografico);

        return acervoCartograficoRepository.save(acervoCartografico);
    }

    @Transactional(readOnly = true)
    public Optional<AcervoCartografico> findById(Long id) {
        acervoCartograficoValidation.validateFindById(id);
        return acervoCartograficoRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        acervoCartograficoValidation.validateDelete(id, acervoCartograficoRepository);
        acervoCartograficoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AcervoCartografico> search(AcervoCartograficoSearchFilter filtro) {
        Specification<AcervoCartografico> spec = AcervoCartograficoSpecification.searchByFields(filtro);
        return acervoCartograficoRepository.findAll(spec);
    }

    // PATCH: so os campos presentes no corpo (nao-nulos) sobrescrevem o registro
    // existente — os demais mantem o valor atual. Depois do merge, valida o
    // resultado combinado (que precisa ter todos os campos obrigatorios preenchidos).
    @Transactional
    public AcervoCartografico update(AcervoCartografico acervoCartografico) {
        AcervoCartografico existente = acervoCartograficoValidation.fetchExistenteParaUpdate(
                acervoCartografico.getId(), acervoCartograficoRepository);

        aplicarPatch(existente, acervoCartografico);

        acervoCartograficoValidation.validateUpdate(existente, acervoCartograficoRepository, tipoDocumentoRepository, entidadeProdutoraRepository);

        rehydrateRelationships(existente);

        return acervoCartograficoRepository.save(existente);
    }

    private void aplicarPatch(AcervoCartografico existente, AcervoCartografico patch) {
        if (patch.getTipoDocumento() != null) {
            existente.setTipoDocumento(patch.getTipoDocumento());
        }
        if (patch.getCodigoIdentificacao() != null) {
            existente.setCodigoIdentificacao(patch.getCodigoIdentificacao());
        }
        if (patch.getTitulo() != null) {
            existente.setTitulo(patch.getTitulo());
        }
        if (patch.getDimensao() != null) {
            existente.setDimensao(patch.getDimensao());
        }
        if (patch.getLocalizacao() != null) {
            existente.setLocalizacao(patch.getLocalizacao());
        }
        if (patch.getLocalidade() != null) {
            existente.setLocalidade(patch.getLocalidade());
        }
        if (patch.getAno() != null) {
            existente.setAno(patch.getAno());
        }
        if (patch.getQuantidadeVolume() != null) {
            existente.setQuantidadeVolume(patch.getQuantidadeVolume());
        }
        if (patch.getDisponibilidade() != null) {
            existente.setDisponibilidade(patch.getDisponibilidade());
        }
        if (patch.getEntidadeProdutora() != null) {
            existente.setEntidadeProdutora(patch.getEntidadeProdutora());
        }
    }

    private void rehydrateRelationships(AcervoCartografico acervoCartografico) {
        if (acervoCartografico.getTipoDocumento() != null && acervoCartografico.getTipoDocumento().getId() != null) {
            acervoCartografico.setTipoDocumento(
                    tipoDocumentoRepository.findById(acervoCartografico.getTipoDocumento().getId()).orElse(null)
            );
        }
        if (acervoCartografico.getEntidadeProdutora() != null && acervoCartografico.getEntidadeProdutora().getId() != null) {
            acervoCartografico.setEntidadeProdutora(
                    entidadeProdutoraRepository.findById(acervoCartografico.getEntidadeProdutora().getId()).orElse(null)
            );
        }
    }
}
