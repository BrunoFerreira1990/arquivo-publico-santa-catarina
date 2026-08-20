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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

// Importa explicitamente a config de auditoria (@EnableJpaAuditing) porque o slice
// @DataJpaTest, por padrão, nao carrega @Configuration da aplicacao.
@DataJpaTest
@Import(com.example.apesc.config.JpaConfig.class)
class RegistroConsultaRepositoryTest {

    @Autowired
    private RegistroConsultaRepository registroConsultaRepository;

    @Autowired
    private RegistroConsultaItemRepository registroConsultaItemRepository;

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

    private AcervoDocumental umAcervoDocumentalPersistivel() {
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
        return acervoDocumentalRepository.save(acervo);
    }

    // Registro persistivel com exatamente 1 item documental, pra manter os testes de
    // duplicidade comparaveis com o comportamento anterior (1 registro = 1 acervo).
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

        AcervoDocumental acervo = umAcervoDocumentalPersistivel();

        RegistroConsulta registro = new RegistroConsulta();
        registro.setPesquisador(pesquisador);
        registro.setDataPesquisa(LocalDate.of(2026, 8, 14));
        registro.setTipoConsulta(TipoConsulta.PRESENCIAL);
        registro.setFuncionario(funcionario);

        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setAcervoDocumental(acervo);
        item.setPeriodo("Manhã");
        item.setQuantidade(3);
        registro.addItem(item);

        return registro;
    }

    private RegistroConsultaItem primeiroItem(RegistroConsulta registro) {
        return registro.getItens().iterator().next();
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

    @Test
    void save_devePersistirOsItensJuntoComORegistroViaCascade() {
        RegistroConsulta registro = umRegistroPersistivel();

        RegistroConsulta salvo = registroConsultaRepository.saveAndFlush(registro);

        assertThat(salvo.getItens()).hasSize(1);
        assertThat(primeiroItem(salvo).getId()).isNotNull();
        assertThat(primeiroItem(salvo).getAcervoCartografico()).isNull();
        assertThat(registroConsultaItemRepository.findAll()).hasSize(1);
    }

    @Test
    void findByIdWithRelations_deveTrazerItensJaCarregadosSemLancarLazyInitializationException() {
        RegistroConsulta registro = umRegistroPersistivel();
        Long id = registroConsultaRepository.saveAndFlush(registro).getId();

        // Simula o fim da transacao/sessao (open-in-view=false): limpa o contexto de
        // persistencia entre o save e a busca, antes de acessar "itens".
        entityManager.clear();

        RegistroConsulta encontrado = registroConsultaRepository.findByIdWithRelations(id).orElseThrow();
        entityManager.clear();

        assertThatCode(() -> encontrado.getItens().size()).doesNotThrowAnyException();
        assertThat(encontrado.getItens()).hasSize(1);
    }

    private boolean existeDuplicadoDocumental(RegistroConsulta registro, Long idAtual) {
        RegistroConsultaItem item = primeiroItem(registro);
        return registroConsultaItemRepository.existsDuplicadoDocumental(
                registro.getPesquisador().getId(),
                registro.getDataPesquisa(),
                registro.getTipoConsulta(),
                item.getAcervoDocumental().getId(),
                item.getPeriodo(),
                item.getQuantidade(),
                idAtual
        );
    }

    @Test
    void existsDuplicadoDocumental_deveRetornarTrueQuandoJaExisteItemIdenticoENenhumRegistroEhExcluido() {
        RegistroConsulta registro = umRegistroPersistivel();
        registroConsultaRepository.saveAndFlush(registro);

        // idAtual nulo simula o cenario de create: nao ha ID proprio a excluir da busca.
        assertThat(existeDuplicadoDocumental(registro, null)).isTrue();
    }

    @Test
    void existsDuplicadoDocumental_deveRetornarFalseQuandoAlgumCampoDeNegocioDifere() {
        RegistroConsulta registro = umRegistroPersistivel();
        registroConsultaRepository.saveAndFlush(registro);

        RegistroConsulta comparacao = umRegistroPersistivel();
        comparacao.setPesquisador(registro.getPesquisador());
        comparacao.setFuncionario(registro.getFuncionario());
        primeiroItem(comparacao).setAcervoDocumental(primeiroItem(registro).getAcervoDocumental());
        primeiroItem(comparacao).setPeriodo("Tarde"); // único campo diferente

        assertThat(existeDuplicadoDocumental(comparacao, null)).isFalse();
    }

    @Test
    void existsDuplicadoDocumental_deveRetornarTrueMesmoComFuncionarioDiferente() {
        RegistroConsulta registro = umRegistroPersistivel();
        registroConsultaRepository.saveAndFlush(registro);

        Funcionario outroFuncionario = new Funcionario();
        outroFuncionario.setNome("Ana Paula Souza");
        outroFuncionario.setDataNascimento(LocalDate.of(1992, 3, 10));
        outroFuncionario.setGenero(Generos.FEMININO);
        outroFuncionario.setEmail("ana@apesc.sc.gov.br");
        outroFuncionario.setNumeroMatricula("456");
        outroFuncionario.setCargo("Arquivista");
        outroFuncionario.setSetor("Acervo");
        outroFuncionario = funcionarioRepository.save(outroFuncionario);

        RegistroConsulta comparacao = umRegistroPersistivel();
        comparacao.setPesquisador(registro.getPesquisador());
        comparacao.setFuncionario(outroFuncionario); // unico campo diferente
        primeiroItem(comparacao).setAcervoDocumental(primeiroItem(registro).getAcervoDocumental());

        // funcionario nao entra na comparacao — outra pessoa registrando os mesmos
        // dados tambem e barrada como duplicidade.
        assertThat(existeDuplicadoDocumental(comparacao, null)).isTrue();
    }

    @Test
    void existsDuplicadoDocumental_deveRetornarFalseQuandoOUnicoItemIdenticoEhDoProprioRegistroSendoEditado() {
        RegistroConsulta registro = umRegistroPersistivel();
        registroConsultaRepository.saveAndFlush(registro);

        // update: excluindo o proprio ID do registro, nao sobra nenhum item de OUTRO
        // registro identico -> permitido.
        assertThat(existeDuplicadoDocumental(registro, registro.getId())).isFalse();
    }

    @Test
    void existsDuplicadoDocumental_deveRetornarTrueQuandoOutroRegistroComIdDiferenteEhIdenticoMesmoCriadoEmOutroDia() {
        // registro A: cadastrado ontem (backdatado direto no banco, ja que updatable=false
        // impede alterar dataRegistro via entidade).
        RegistroConsulta registroA = umRegistroPersistivel();
        registroA = registroConsultaRepository.saveAndFlush(registroA);
        entityManager.createQuery("UPDATE RegistroConsulta rc SET rc.dataRegistro = :ontem WHERE rc.id = :id")
                .setParameter("ontem", LocalDate.now().minusDays(1).atTime(10, 0))
                .setParameter("id", registroA.getId())
                .executeUpdate();
        entityManager.clear();

        // registro B: mesmos dados de negocio de A (mesmo pesquisador, funcionario e
        // acervo/periodo/quantidade do item), cadastrado hoje.
        RegistroConsulta registroB = umRegistroPersistivel();
        registroB.setPesquisador(pesquisadorRepository.findById(registroA.getPesquisador().getId()).orElseThrow());
        registroB.setFuncionario(funcionarioRepository.findById(registroA.getFuncionario().getId()).orElseThrow());
        AcervoDocumental acervoDeA = primeiroItem(registroA).getAcervoDocumental();
        primeiroItem(registroB).setAcervoDocumental(acervoDocumentalRepository.findById(acervoDeA.getId()).orElseThrow());
        registroB = registroConsultaRepository.saveAndFlush(registroB);

        // editando B (excluindo o ID de B da busca): mesmo A tendo sido cadastrado em outro
        // dia, ele ainda e encontrado e bloqueia a edicao — fecha o gap de dia diferente.
        assertThat(existeDuplicadoDocumental(registroB, registroB.getId())).isTrue();
    }
}
