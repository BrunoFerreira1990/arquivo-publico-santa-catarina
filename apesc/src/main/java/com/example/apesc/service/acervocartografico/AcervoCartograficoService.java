package com.example.apesc.service.acervocartografico;

import com.example.apesc.model.AcervoCartografico;
import com.example.apesc.specification.AcervoCartograficoSearchFilter;

import java.util.List;
import java.util.Optional;

public interface AcervoCartograficoService {

    AcervoCartografico save(AcervoCartografico acervoCartografico);

    Optional<AcervoCartografico> findById(Long id);

    void delete(Long id);

    AcervoCartografico update(AcervoCartografico acervoCartografico);

    List<AcervoCartografico> search(AcervoCartograficoSearchFilter filtro);
}
