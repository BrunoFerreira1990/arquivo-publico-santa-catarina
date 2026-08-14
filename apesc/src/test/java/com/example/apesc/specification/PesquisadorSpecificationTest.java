package com.example.apesc.specification;

import com.example.apesc.model.Pesquisador;
import com.example.apesc.model.enums.*;
import com.example.apesc.repository.PesquisadorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PesquisadorSpecificationTest {

    @Autowired
    private PesquisadorRepository pesquisadorRepository;

    private Pesquisador salvar(String nome, String cpf) {
        Pesquisador p = new Pesquisador();
        p.setNome(nome);
        p.setCpf(cpf);
        p.setDataNascimento(LocalDate.of(1990, 1, 1));
        p.setGenero(Generos.FEMININO);
        p.setNacionalidade(Nacionalidade.BRASILEIRA);
        p.setNumeroTelefone("48999998888");
        p.setLogradouro("Rua das Flores");
        p.setNumeroCasa("123");
        p.setBairro("Centro");
        p.setCidade("Florianópolis");
        p.setEstado(Estados.SC);
        p.setCep("88010000");
        p.setNivelEducacional(NivelEducacional.ENSINO_SUPERIOR);
        p.setProfissao("Historiadora");
        p.setAssuntoPesquisa("Assunto");
        p.setFinalidadePesquisa("Finalidade");
        p.setPeriodoEstudo(Set.of(PeriodoEstudo.REPUBLICANO));
        p.setAreaEstudo(Set.of(AreaEstudo.HISTORIA_REGIONAL));
        return pesquisadorRepository.save(p);
    }

    @Test
    void searchByFields_devePesquisarPorNomeParcialSemDistinguirMaiusculas() {
        salvar("Bruno Ferreira", "11111111111");
        salvar("Maria Santos", "22222222222");

        Specification<Pesquisador> spec = PesquisadorSpecification.searchByFields("BRUNO", null);
        List<Pesquisador> resultado = pesquisadorRepository.findAll(spec);

        assertThat(resultado).extracting(Pesquisador::getNome).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_devePesquisarPorCpfIgnorandoPontuacaoDoRegistroSalvo() {
        salvar("Bruno Ferreira", "123.456.789-30");

        Specification<Pesquisador> spec = PesquisadorSpecification.searchByFields(null, "12345678930");
        List<Pesquisador> resultado = pesquisadorRepository.findAll(spec);

        assertThat(resultado).extracting(Pesquisador::getNome).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_devePesquisarPorCpfIgnorandoPontuacaoDoParametroDeBusca() {
        salvar("Bruno Ferreira", "12345678930");

        Specification<Pesquisador> spec = PesquisadorSpecification.searchByFields(null, "123.456.789-30");
        List<Pesquisador> resultado = pesquisadorRepository.findAll(spec);

        assertThat(resultado).extracting(Pesquisador::getNome).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_deveCombinarNomeECpfComAnd() {
        salvar("Bruno Ferreira", "11111111111");
        salvar("Bruno Souza", "22222222222");

        Specification<Pesquisador> spec = PesquisadorSpecification.searchByFields("Bruno", "11111111111");
        List<Pesquisador> resultado = pesquisadorRepository.findAll(spec);

        assertThat(resultado).extracting(Pesquisador::getNome).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_deveRetornarTodosQuandoNenhumFiltroInformado() {
        salvar("Bruno Ferreira", "11111111111");
        salvar("Maria Santos", "22222222222");

        Specification<Pesquisador> spec = PesquisadorSpecification.searchByFields(null, null);
        List<Pesquisador> resultado = pesquisadorRepository.findAll(spec);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void searchByFields_deveRetornarVazioQuandoNomeNaoCasaComNenhumRegistro() {
        salvar("Bruno Ferreira", "11111111111");

        Specification<Pesquisador> spec = PesquisadorSpecification.searchByFields("Inexistente", null);
        List<Pesquisador> resultado = pesquisadorRepository.findAll(spec);

        assertThat(resultado).isEmpty();
    }
}
