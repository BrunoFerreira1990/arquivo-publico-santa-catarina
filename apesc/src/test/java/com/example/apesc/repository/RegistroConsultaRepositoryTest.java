package com.example.apesc.repository;

import com.example.apesc.model.*;
import com.example.apesc.model.enums.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

// Importa explicitamente a config de auditoria (@EnableJpaAuditing) porque o slice
// @DataJpaTest, por padrão, nao carrega @Configuration da aplicacao.
@DataJpaTest
@Import(com.example.apesc.config.JpaConfig.class)
class RegistroConsultaRepositoryTest {

    @Autowired
    private RegistroConsultaRepository registroConsultaRepository;

    @Autowired
    private PesquisadorRepository pesquisadorRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AcervoDocumentalRepository acervoDocumentalRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private EntidadeProdutoraRepository entidadeProdutoraRepository;

    @Autowired
    private EntityManager entityManager;

    private RegistroConsulta umRegistroPersistivel() {
        Pesquisador pesquisador = new Pesquisador();
        pesquisador.setNome("Maria Aparecida Silva");
        pesquisador.setCpf("12345678930");
        pesquisador.setDataNascimento(LocalDate.of(1990, 1, 1));
        pesquisador.setGenero(Generos.FEMININO);
        pesquisador.setNacionalidade(Nacionalidade.BRASILEIRA);
        pesquisador.setNumeroTelefone("48999998888");
        pesquisador.setLogradouro("Rua das Flores");
        pesquisador.setNumeroCasa("123");
        pesquisador.setBairro("Centro");
        pesquisador.setCidade("Florianópolis");
        pesquisador.setEstado(Estados.SC);
        pesquisador.setCep("88010000");
        pesquisador.setNivelEducacional(NivelEducacional.ENSINO_SUPERIOR);
        pesquisador.setProfissao("Historiadora");
        pesquisador.setAssuntoPesquisa("Assunto");
        pesquisador.setFinalidadePesquisa("Finalidade");
        pesquisador = pesquisadorRepository.save(pesquisador);

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João da Silva");
        funcionario.setDataNascimento(LocalDate.of(1985, 5, 20));
        funcionario.setGenero(Generos.MASCULINO);
        funcionario.setEmail("joao@apesc.sc.gov.br");
        funcionario.setNumeroMatricula("123");
        funcionario.setCargo("Arquivista");
        funcionario.setSetor("Acervo");
        funcionario = funcionarioRepository.save(funcionario);

        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNomeDocumento("Ofício");
        tipoDocumento = tipoDocumentoRepository.save(tipoDocumento);

        EntidadeProdutora entidadeProdutora = new EntidadeProdutora();
        entidadeProdutora.setNome("Secretaria de Estado");
        entidadeProdutora.setAbreviacao("SE");
        entidadeProdutora = entidadeProdutoraRepository.save(entidadeProdutora);

        AcervoDocumental acervo = new AcervoDocumental();
        acervo.setTipoDocumento(tipoDocumento);
        acervo.setEntidadeProdutora(entidadeProdutora);
        acervo.setNaturezaTransacao(NaturezaTransacao.EXPEDIDOS);
        acervo.setPeriodo("2020");
        acervo.setEstante("A1");
        acervo.setQuantidade(1);
        acervo.setDisponibilidade(true);
        acervo = acervoDocumentalRepository.save(acervo);

        RegistroConsulta registro = new RegistroConsulta();
        registro.setPesquisador(pesquisador);
        registro.setDataPesquisa(LocalDate.of(2026, 8, 14));
        registro.setTipoConsulta(TipoConsulta.PRESENCIAL);
        registro.setAcervoDocumental(acervo);
        registro.setPeriodo("Manhã");
        registro.setQuantidade(3);
        registro.setFuncionario(funcionario);
        return registro;
    }

    @Test
    void save_devePreencherDataRegistroAutomaticamenteSemPrecisarSerInformada() {
        RegistroConsulta registro = umRegistroPersistivel();
        assertThat(registro.getDataRegistro()).isNull();

        RegistroConsulta salvo = registroConsultaRepository.saveAndFlush(registro);

        assertThat(salvo.getDataRegistro()).isNotNull();
    }

    @Test
    void save_deveIgnorarDataRegistroEnviadaManualmenteEUsarADoMomentoDoSave() {
        RegistroConsulta registro = umRegistroPersistivel();
        // mesmo que alguem tente forcar uma data de registro manualmente antes do save...
        registro.setDataRegistro(LocalDateTime.of(2000, 1, 1, 0, 0));

        RegistroConsulta salvo = registroConsultaRepository.saveAndFlush(registro);

        // ...a auditoria do JPA sobrescreve com o instante real da persistencia.
        assertThat(salvo.getDataRegistro()).isAfter(LocalDateTime.of(2020, 1, 1, 0, 0));
    }

    @Test
    void save_naoDevePreencherDataAtualizacaoNaCriacao() {
        RegistroConsulta registro = umRegistroPersistivel();

        RegistroConsulta salvo = registroConsultaRepository.saveAndFlush(registro);

        assertThat(salvo.getDataAtualizacao()).isNull();
    }

    private boolean existeDuplicadoHoje(RegistroConsulta registro) {
        LocalDate hoje = LocalDate.now();
        return registroConsultaRepository.existsDuplicadoNoMesmoDia(
                registro.getPesquisador().getId(),
                registro.getDataPesquisa(),
                registro.getTipoConsulta(),
                registro.getAcervoDocumental().getId(),
                registro.getPeriodo(),
                registro.getQuantidade(),
                registro.getFuncionario().getId(),
                hoje.atStartOfDay(),
                hoje.atTime(LocalTime.MAX)
        );
    }

    @Test
    void existsDuplicadoNoMesmoDia_deveRetornarTrueQuandoJaExisteRegistroIdenticoCriadoHoje() {
        RegistroConsulta registro = umRegistroPersistivel();
        registroConsultaRepository.saveAndFlush(registro);

        assertThat(existeDuplicadoHoje(registro)).isTrue();
    }

    @Test
    void existsDuplicadoNoMesmoDia_deveRetornarFalseQuandoAlgumCampoDeNegocioDifere() {
        RegistroConsulta registro = umRegistroPersistivel();
        registroConsultaRepository.saveAndFlush(registro);

        RegistroConsulta comparacao = umRegistroPersistivel();
        comparacao.setPesquisador(registro.getPesquisador());
        comparacao.setAcervoDocumental(registro.getAcervoDocumental());
        comparacao.setFuncionario(registro.getFuncionario());
        comparacao.setPeriodo("Tarde"); // único campo diferente

        assertThat(existeDuplicadoHoje(comparacao)).isFalse();
    }

    @Test
    void existsDuplicadoNoMesmoDia_deveRetornarFalseQuandoRegistroIdenticoFoiCriadoEmOutroDia() {
        RegistroConsulta registro = umRegistroPersistivel();
        registroConsultaRepository.saveAndFlush(registro);

        // backdata o registro pra ontem direto no banco (updatable=false impede via entidade)
        entityManager.createQuery("UPDATE RegistroConsulta rc SET rc.dataRegistro = :ontem WHERE rc.id = :id")
                .setParameter("ontem", LocalDate.now().minusDays(1).atTime(10, 0))
                .setParameter("id", registro.getId())
                .executeUpdate();
        entityManager.clear();

        assertThat(existeDuplicadoHoje(registro)).isFalse();
    }
}
