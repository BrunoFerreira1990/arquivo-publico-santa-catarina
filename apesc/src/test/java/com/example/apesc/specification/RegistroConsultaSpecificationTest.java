package com.example.apesc.specification;

import com.example.apesc.model.*;
import com.example.apesc.model.enums.*;
import com.example.apesc.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

// Importa explicitamente a config de auditoria (@EnableJpaAuditing) porque o slice
// @DataJpaTest, por padrão, nao carrega @Configuration da aplicacao.
@DataJpaTest
@Import(com.example.apesc.config.JpaConfig.class)
class RegistroConsultaSpecificationTest {

    @Autowired
    private RegistroConsultaRepository registroConsultaRepository;

    @Autowired
    private PesquisadorRepository pesquisadorRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AcervoDocumentalRepository acervoDocumentalRepository;

    @Autowired
    private AcervoDocumentalProcessosRepository acervoDocumentalProcessosRepository;

    @Autowired
    private AcervoIconograficoRepository acervoIconograficoRepository;

    @Autowired
    private AcervoIconograficoAssuntosRepository acervoIconograficoAssuntosRepository;

    @Autowired
    private AcervoCartograficoRepository acervoCartograficoRepository;

    @Autowired
    private BibliotecaLivrosPeriodicosRepository bibliotecaLivrosPeriodicosRepository;

    @Autowired
    private BibliotecaApoioRepository bibliotecaApoioRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private EntidadeProdutoraRepository entidadeProdutoraRepository;

    @Autowired
    private EntityManager entityManager;

    // ---------- fixtures ----------

    private TipoDocumento umTipoDocumento(String nome) {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNomeDocumento(nome);
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    private EntidadeProdutora umaEntidadeProdutora(String nome) {
        EntidadeProdutora entidadeProdutora = new EntidadeProdutora();
        entidadeProdutora.setNome(nome);
        entidadeProdutora.setAbreviacao(nome.substring(0, Math.min(3, nome.length())).toUpperCase());
        return entidadeProdutoraRepository.save(entidadeProdutora);
    }

    private AcervoDocumental umAcervoDocumental(TipoDocumento tipoDocumento, EntidadeProdutora produtora, EntidadeProdutora receptora) {
        AcervoDocumental acervo = new AcervoDocumental();
        acervo.setTipoDocumento(tipoDocumento);
        acervo.setEntidadeProdutora(produtora);
        acervo.setEntidadeReceptora(receptora);
        acervo.setNaturezaTransacao(receptora != null ? NaturezaTransacao.EXPEDIDOS_PARA : NaturezaTransacao.EXPEDIDOS);
        acervo.setPeriodo("2020");
        acervo.setEstante("A1");
        acervo.setQuantidade(1);
        acervo.setDisponibilidade(true);
        return acervoDocumentalRepository.save(acervo);
    }

    private AcervoDocumentalProcessos umAcervoDocumentalProcessos(AcervoDocumental acervoDocumental, String nomeProcesso) {
        AcervoDocumentalProcessos processo = new AcervoDocumentalProcessos();
        processo.setAcervoDocumental(acervoDocumental);
        processo.setCaixaIdentificacao("CX-01");
        processo.setLocalizacao("Estante C1");
        processo.setNomeProcesso(nomeProcesso);
        processo.setData("2020");
        processo.setIdentificacaoPasta("P-01");
        processo.setDisponibilidade(true);
        return acervoDocumentalProcessosRepository.save(processo);
    }

    private AcervoIconografico umAcervoIconografico(TipoDocumento tipoDocumento, String titulo, String codigo) {
        AcervoIconograficoAssuntos assunto = new AcervoIconograficoAssuntos();
        assunto.setAssuntos("História Regional");
        assunto = acervoIconograficoAssuntosRepository.save(assunto);

        AcervoIconografico acervo = new AcervoIconografico();
        acervo.setTipoDocumento(tipoDocumento);
        acervo.setCodigoIdentificacao(codigo);
        acervo.setTitulo(titulo);
        acervo.setLocalizacao("Estante D1");
        acervo.setDisponibilidade(true);
        acervo.setAno("1950");
        acervo.setAssuntos(Set.of(assunto));
        return acervoIconograficoRepository.save(acervo);
    }

    private AcervoCartografico umAcervoCartografico(TipoDocumento tipoDocumento, EntidadeProdutora produtora, String titulo, String codigo) {
        AcervoCartografico acervo = new AcervoCartografico();
        acervo.setTipoDocumento(tipoDocumento);
        acervo.setEntidadeProdutora(produtora);
        acervo.setCodigoIdentificacao(codigo);
        acervo.setTitulo(titulo);
        acervo.setDimensao("60x80cm");
        acervo.setLocalizacao("Estante B1");
        acervo.setAno("1900");
        acervo.setQuantidadeVolume(1);
        acervo.setDisponibilidade(true);
        return acervoCartograficoRepository.save(acervo);
    }

    private BibliotecaLivrosPeriodicos umBibliotecaLivro(TipoDocumento tipoDocumento, String titulo, String autores) {
        BibliotecaLivrosPeriodicos livro = new BibliotecaLivrosPeriodicos();
        livro.setTipoDocumento(tipoDocumento);
        livro.setTitulo(titulo);
        livro.setAutores(autores);
        livro.setEditora("Editora Teste");
        livro.setLocalizacao("Estante E1");
        livro.setQuantidadeExemplar(1);
        livro.setDisponibilidade(true);
        return bibliotecaLivrosPeriodicosRepository.save(livro);
    }

    private BibliotecaApoio umBibliotecaApoio(TipoDocumento tipoDocumento, EntidadeProdutora produtora, String titulo, String identificador) {
        BibliotecaApoio apoio = new BibliotecaApoio();
        apoio.setTipoDocumento(tipoDocumento);
        apoio.setEntidadeProdutora(produtora);
        apoio.setTitulo(titulo);
        apoio.setPeriodo("2020");
        apoio.setQuantidadeVolume(1);
        apoio.setIdentificador(identificador);
        apoio.setLocalizacao("Estante F1");
        apoio.setDisponibilidade(true);
        return bibliotecaApoioRepository.save(apoio);
    }

    private Pesquisador umPesquisador(String nome, String cpf) {
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
        return pesquisadorRepository.save(p);
    }

    private Funcionario umFuncionario(String nome, String matricula) {
        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setDataNascimento(LocalDate.of(1985, 5, 20));
        f.setGenero(Generos.MASCULINO);
        f.setEmail(matricula + "@apesc.sc.gov.br");
        f.setNumeroMatricula(matricula);
        f.setCargo("Arquivista");
        f.setSetor("Acervo");
        return funcionarioRepository.save(f);
    }

    private RegistroConsulta registroBase(Pesquisador pesquisador, Funcionario funcionario, LocalDate dataPesquisa,
                                           TipoConsulta tipoConsulta, Boolean semConsulta) {
        RegistroConsulta registro = new RegistroConsulta();
        registro.setPesquisador(pesquisador);
        registro.setDataPesquisa(dataPesquisa);
        registro.setTipoConsulta(tipoConsulta);
        registro.setFuncionario(funcionario);
        registro.setSemConsulta(semConsulta);
        return registro;
    }

    private RegistroConsulta umRegistroConsulta(Pesquisador pesquisador, Funcionario funcionario,
                                                 LocalDate dataPesquisa, TipoConsulta tipoConsulta,
                                                 Boolean semConsulta, AcervoDocumental acervo) {
        RegistroConsulta registro = registroBase(pesquisador, funcionario, dataPesquisa, tipoConsulta, semConsulta);

        if (acervo != null) {
            RegistroConsultaItem item = new RegistroConsultaItem();
            item.setAcervoDocumental(acervo);
            item.setPeriodo("Manhã");
            item.setQuantidade(1);
            registro.addItem(item);
        }

        return registroConsultaRepository.save(registro);
    }

    // ---------- builder do filtro (23 campos — evita chamadas com dezenas de nulls posicionais) ----------

    private static class FiltroBuilder {
        private LocalDate dataPesquisa;
        private LocalDate dataPesquisaInicio;
        private LocalDate dataPesquisaFim;
        private String nomePesquisador;
        private String nomeFuncionario;
        private TipoConsulta tipoConsulta;
        private Boolean semConsulta;
        private String tipoDocumentoNomeDocumental;
        private String entidadeProdutoraNomeDocumental;
        private String entidadeReceptoraNomeDocumental;
        private String nomeProcessoDocumentalProcessos;
        private String tituloIconografico;
        private String codigoIconografico;
        private String tipoDocumentoNomeIconografico;
        private String tituloCartografico;
        private String codigoCartografico;
        private String tipoDocumentoNomeCartografico;
        private String entidadeProdutoraNomeCartografico;
        private String tituloBibliotecaLivros;
        private String autoresBibliotecaLivros;
        private String tipoDocumentoNomeBibliotecaLivros;
        private String tituloBibliotecaApoio;
        private String identificadorBibliotecaApoio;
        private String tipoDocumentoNomeBibliotecaApoio;
        private String entidadeProdutoraNomeBibliotecaApoio;

        FiltroBuilder dataPesquisa(LocalDate v) { this.dataPesquisa = v; return this; }
        FiltroBuilder dataPesquisaInicio(LocalDate v) { this.dataPesquisaInicio = v; return this; }
        FiltroBuilder dataPesquisaFim(LocalDate v) { this.dataPesquisaFim = v; return this; }
        FiltroBuilder nomePesquisador(String v) { this.nomePesquisador = v; return this; }
        FiltroBuilder nomeFuncionario(String v) { this.nomeFuncionario = v; return this; }
        FiltroBuilder tipoConsulta(TipoConsulta v) { this.tipoConsulta = v; return this; }
        FiltroBuilder semConsulta(Boolean v) { this.semConsulta = v; return this; }
        FiltroBuilder tipoDocumentoNomeDocumental(String v) { this.tipoDocumentoNomeDocumental = v; return this; }
        FiltroBuilder entidadeProdutoraNomeDocumental(String v) { this.entidadeProdutoraNomeDocumental = v; return this; }
        FiltroBuilder entidadeReceptoraNomeDocumental(String v) { this.entidadeReceptoraNomeDocumental = v; return this; }
        FiltroBuilder nomeProcessoDocumentalProcessos(String v) { this.nomeProcessoDocumentalProcessos = v; return this; }
        FiltroBuilder tituloIconografico(String v) { this.tituloIconografico = v; return this; }
        FiltroBuilder codigoIconografico(String v) { this.codigoIconografico = v; return this; }
        FiltroBuilder tipoDocumentoNomeIconografico(String v) { this.tipoDocumentoNomeIconografico = v; return this; }
        FiltroBuilder tituloCartografico(String v) { this.tituloCartografico = v; return this; }
        FiltroBuilder codigoCartografico(String v) { this.codigoCartografico = v; return this; }
        FiltroBuilder tipoDocumentoNomeCartografico(String v) { this.tipoDocumentoNomeCartografico = v; return this; }
        FiltroBuilder entidadeProdutoraNomeCartografico(String v) { this.entidadeProdutoraNomeCartografico = v; return this; }
        FiltroBuilder tituloBibliotecaLivros(String v) { this.tituloBibliotecaLivros = v; return this; }
        FiltroBuilder autoresBibliotecaLivros(String v) { this.autoresBibliotecaLivros = v; return this; }
        FiltroBuilder tipoDocumentoNomeBibliotecaLivros(String v) { this.tipoDocumentoNomeBibliotecaLivros = v; return this; }
        FiltroBuilder tituloBibliotecaApoio(String v) { this.tituloBibliotecaApoio = v; return this; }
        FiltroBuilder identificadorBibliotecaApoio(String v) { this.identificadorBibliotecaApoio = v; return this; }
        FiltroBuilder tipoDocumentoNomeBibliotecaApoio(String v) { this.tipoDocumentoNomeBibliotecaApoio = v; return this; }
        FiltroBuilder entidadeProdutoraNomeBibliotecaApoio(String v) { this.entidadeProdutoraNomeBibliotecaApoio = v; return this; }

        RegistroConsultaSearchFilter build() {
            return new RegistroConsultaSearchFilter(
                    dataPesquisa, dataPesquisaInicio, dataPesquisaFim, nomePesquisador, nomeFuncionario, tipoConsulta, semConsulta,
                    tipoDocumentoNomeDocumental, entidadeProdutoraNomeDocumental, entidadeReceptoraNomeDocumental,
                    nomeProcessoDocumentalProcessos,
                    tituloIconografico, codigoIconografico, tipoDocumentoNomeIconografico,
                    tituloCartografico, codigoCartografico, tipoDocumentoNomeCartografico, entidadeProdutoraNomeCartografico,
                    tituloBibliotecaLivros, autoresBibliotecaLivros, tipoDocumentoNomeBibliotecaLivros,
                    tituloBibliotecaApoio, identificadorBibliotecaApoio, tipoDocumentoNomeBibliotecaApoio, entidadeProdutoraNomeBibliotecaApoio
            );
        }
    }

    private List<RegistroConsulta> buscar(FiltroBuilder filtro) {
        return registroConsultaRepository.findAll(RegistroConsultaSpecification.searchByFields(filtro.build()));
    }

    // ---------- filtros basicos do registro ----------

    @Test
    void searchByFields_devePesquisarPorNomeDoPesquisadorParcialSemDistinguirMaiusculas() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(umPesquisador("Maria Santos", "22222222222"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().nomePesquisador("BRUNO"));

        assertThat(resultado).extracting(r -> r.getPesquisador().getNome()).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_devePesquisarPorNomeDoFuncionarioParcialSemDistinguirMaiusculas() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");

        umRegistroConsulta(pesquisador, umFuncionario("João da Silva", "111"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, umFuncionario("Ana Souza", "222"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().nomeFuncionario("joão"));

        assertThat(resultado).extracting(r -> r.getFuncionario().getNome()).containsExactly("João da Silva");
    }

    @Test
    void searchByFields_devePesquisarPorDataPesquisaExata() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 19), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().dataPesquisa(LocalDate.of(2026, 8, 18)));

        assertThat(resultado).extracting(RegistroConsulta::getDataPesquisa).containsExactly(LocalDate.of(2026, 8, 18));
    }

    @Test
    void searchByFields_devePesquisarPorIntervaloDeDatas() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 10), TipoConsulta.PRESENCIAL, false, acervo); // antes do intervalo
        RegistroConsulta dentro = umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 30), TipoConsulta.PRESENCIAL, false, acervo); // depois do intervalo

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder()
                .dataPesquisaInicio(LocalDate.of(2026, 8, 15))
                .dataPesquisaFim(LocalDate.of(2026, 8, 20)));

        assertThat(resultado).extracting(RegistroConsulta::getId).containsExactly(dentro.getId());
    }

    @Test
    void searchByFields_devePesquisarSoComDataInicioAbertaAteHoje() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 10), TipoConsulta.PRESENCIAL, false, acervo);
        RegistroConsulta depois = umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        // so dataPesquisaInicio, sem fim — pega tudo dali pra frente
        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().dataPesquisaInicio(LocalDate.of(2026, 8, 15)));

        assertThat(resultado).extracting(RegistroConsulta::getId).containsExactly(depois.getId());
    }

    @Test
    void searchByFields_devePesquisarPorTipoConsulta() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.REMOTO, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().tipoConsulta(TipoConsulta.REMOTO));

        assertThat(resultado).extracting(RegistroConsulta::getTipoConsulta).containsExactly(TipoConsulta.REMOTO);
    }

    @Test
    void searchByFields_devePesquisarPorSemConsulta() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 19), TipoConsulta.PRESENCIAL, true, null);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().semConsulta(true));

        assertThat(resultado).extracting(RegistroConsulta::getSemConsulta).containsExactly(true);
        assertThat(resultado.get(0).getItens()).isEmpty();
    }

    @Test
    void searchByFields_deveCombinarMultiplosFiltrosComAnd() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        // mesmo pesquisador (nome parecido), mas tipo de consulta diferente — nao deve bater
        umRegistroConsulta(umPesquisador("Bruno Souza", "22222222222"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.REMOTO, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().nomePesquisador("Bruno").tipoConsulta(TipoConsulta.PRESENCIAL).semConsulta(false));

        assertThat(resultado).extracting(r -> r.getPesquisador().getNome()).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_deveRetornarTodosQuandoNenhumFiltroInformado() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(umPesquisador("Maria Santos", "22222222222"), funcionario,
                LocalDate.of(2026, 8, 19), TipoConsulta.REMOTO, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder());

        assertThat(resultado).hasSize(2);
    }

    @Test
    void searchByFields_deveRetornarVazioQuandoFiltroNaoBateComNenhumRegistro() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), umFuncionario("João da Silva", "111"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().nomePesquisador("Inexistente"));

        assertThat(resultado).isEmpty();
    }

    @Test
    void searchByFields_deveTrazerItensJaCarregadosSemLancarLazyInitializationException() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), umFuncionario("João da Silva", "111"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder());

        // Simula o fim da transacao/sessao (open-in-view=false): limpa o contexto de
        // persistencia depois da query, antes de acessar a colecao "itens". Se o fetch
        // da specification nao estivesse la, isso lancaria LazyInitializationException.
        entityManager.clear();

        assertThatCode(() -> resultado.forEach(r -> r.getItens().size())).doesNotThrowAnyException();
        assertThat(resultado.get(0).getItens()).hasSize(1);
    }

    @Test
    void searchByFields_naoDeveDuplicarRegistroComMaisDeUmItem() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental acervoDocumental = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        AcervoCartografico acervoCartografico = umAcervoCartografico(tipoDocumento, null, "Mapa de SC", "MAPA-001");

        RegistroConsulta registro = registroBase(umPesquisador("Bruno Ferreira", "11111111111"),
                umFuncionario("João da Silva", "111"), LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false);

        RegistroConsultaItem itemDocumental = new RegistroConsultaItem();
        itemDocumental.setAcervoDocumental(acervoDocumental);
        itemDocumental.setPeriodo("Manhã");
        itemDocumental.setQuantidade(1);
        registro.addItem(itemDocumental);

        RegistroConsultaItem itemCartografico = new RegistroConsultaItem();
        itemCartografico.setAcervoCartografico(acervoCartografico);
        itemCartografico.setPeriodo("Tarde");
        itemCartografico.setQuantidade(1);
        registro.addItem(itemCartografico);

        registroConsultaRepository.save(registro);

        // Sem o DISTINCT no specification, o fetch de uma colecao 1:N (itens) faria
        // esse registro aparecer 2 vezes no resultado — uma por item.
        List<RegistroConsulta> resultado = buscar(new FiltroBuilder());

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getItens()).hasSize(2);
    }

    // ---------- filtros por atributos de cada tipo de acervo ----------

    @Test
    void searchByFields_devePesquisarAcervoDocumentalPorTipoDocumentoEEntidadeProdutoraEReceptora() {
        TipoDocumento tipoOficio = umTipoDocumento("Ofício");
        TipoDocumento tipoMemo = umTipoDocumento("Memorando");
        EntidadeProdutora produtora = umaEntidadeProdutora("Secretaria de Estado");
        EntidadeProdutora receptora = umaEntidadeProdutora("Prefeitura Municipal");

        AcervoDocumental comOsTres = umAcervoDocumental(tipoOficio, produtora, receptora);
        AcervoDocumental semNadaDisso = umAcervoDocumental(tipoMemo, umaEntidadeProdutora("Outra Entidade"), null);

        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");
        RegistroConsulta alvo = umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, comOsTres);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 19), TipoConsulta.PRESENCIAL, false, semNadaDisso);

        assertThat(buscar(new FiltroBuilder().tipoDocumentoNomeDocumental("ofício")))
                .extracting(RegistroConsulta::getId).containsExactly(alvo.getId());

        assertThat(buscar(new FiltroBuilder().entidadeProdutoraNomeDocumental("secretaria")))
                .extracting(RegistroConsulta::getId).containsExactly(alvo.getId());

        assertThat(buscar(new FiltroBuilder().entidadeReceptoraNomeDocumental("prefeitura")))
                .extracting(RegistroConsulta::getId).containsExactly(alvo.getId());
    }

    @Test
    void searchByFields_devePesquisarAcervoDocumentalProcessosPorNomeDoProcesso() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        AcervoDocumental documental = umAcervoDocumental(tipoDocumento, umaEntidadeProdutora("Secretaria"), null);
        AcervoDocumentalProcessos processo = umAcervoDocumentalProcessos(documental, "Inquérito Administrativo 001");

        RegistroConsulta registro = registroBase(umPesquisador("Bruno Ferreira", "11111111111"),
                umFuncionario("João da Silva", "111"), LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false);
        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setAcervoDocumentalProcessos(processo);
        item.setPeriodo("Manhã");
        item.setQuantidade(1);
        registro.addItem(item);
        registroConsultaRepository.save(registro);

        List<RegistroConsulta> resultado = buscar(new FiltroBuilder().nomeProcessoDocumentalProcessos("inquérito"));

        assertThat(resultado).hasSize(1);
    }

    @Test
    void searchByFields_devePesquisarAcervoIconograficoPorTituloCodigoETipoDocumento() {
        TipoDocumento tipoDocumento = umTipoDocumento("Fotografia");
        AcervoIconografico foto = umAcervoIconografico(tipoDocumento, "Inauguração da Ponte Hercílio Luz", "FOTO-001");

        RegistroConsulta registro = registroBase(umPesquisador("Bruno Ferreira", "11111111111"),
                umFuncionario("João da Silva", "111"), LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false);
        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setAcervoIconografico(foto);
        item.setPeriodo("Manhã");
        item.setQuantidade(1);
        registro.addItem(item);
        registroConsultaRepository.save(registro);

        assertThat(buscar(new FiltroBuilder().tituloIconografico("ponte"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().codigoIconografico("FOTO-001"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().tipoDocumentoNomeIconografico("fotografia"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().tituloIconografico("inexistente"))).isEmpty();
    }

    @Test
    void searchByFields_devePesquisarAcervoCartograficoPorTituloCodigoTipoDocumentoEEntidadeProdutora() {
        TipoDocumento tipoDocumento = umTipoDocumento("Mapa");
        EntidadeProdutora produtora = umaEntidadeProdutora("Instituto Geográfico");
        AcervoCartografico mapa = umAcervoCartografico(tipoDocumento, produtora, "Mapa da Província de Santa Catarina", "MAPA-001");

        RegistroConsulta registro = registroBase(umPesquisador("Bruno Ferreira", "11111111111"),
                umFuncionario("João da Silva", "111"), LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false);
        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setAcervoCartografico(mapa);
        item.setPeriodo("Manhã");
        item.setQuantidade(1);
        registro.addItem(item);
        registroConsultaRepository.save(registro);

        assertThat(buscar(new FiltroBuilder().tituloCartografico("província"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().codigoCartografico("MAPA-001"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().tipoDocumentoNomeCartografico("mapa"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().entidadeProdutoraNomeCartografico("geográfico"))).hasSize(1);
    }

    @Test
    void searchByFields_devePesquisarBibliotecaLivrosPeriodicosPorTituloAutoresETipoDocumento() {
        TipoDocumento tipoDocumento = umTipoDocumento("Livro");
        BibliotecaLivrosPeriodicos livro = umBibliotecaLivro(tipoDocumento, "História de Santa Catarina", "João Autor");

        RegistroConsulta registro = registroBase(umPesquisador("Bruno Ferreira", "11111111111"),
                umFuncionario("João da Silva", "111"), LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false);
        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setBibliotecaLivrosPeriodicos(livro);
        item.setPeriodo("Manhã");
        item.setQuantidade(1);
        registro.addItem(item);
        registroConsultaRepository.save(registro);

        assertThat(buscar(new FiltroBuilder().tituloBibliotecaLivros("história"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().autoresBibliotecaLivros("autor"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().tipoDocumentoNomeBibliotecaLivros("livro"))).hasSize(1);
    }

    @Test
    void searchByFields_devePesquisarBibliotecaApoioPorTituloIdentificadorTipoDocumentoEEntidadeProdutora() {
        TipoDocumento tipoDocumento = umTipoDocumento("Relatório");
        EntidadeProdutora produtora = umaEntidadeProdutora("Fundação Cultural");
        BibliotecaApoio apoio = umBibliotecaApoio(tipoDocumento, produtora, "Relatório Anual de Atividades", "REL-2020");

        RegistroConsulta registro = registroBase(umPesquisador("Bruno Ferreira", "11111111111"),
                umFuncionario("João da Silva", "111"), LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false);
        RegistroConsultaItem item = new RegistroConsultaItem();
        item.setBibliotecaApoio(apoio);
        item.setPeriodo("Manhã");
        item.setQuantidade(1);
        registro.addItem(item);
        registroConsultaRepository.save(registro);

        assertThat(buscar(new FiltroBuilder().tituloBibliotecaApoio("anual"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().identificadorBibliotecaApoio("REL-2020"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().tipoDocumentoNomeBibliotecaApoio("relatório"))).hasSize(1);
        assertThat(buscar(new FiltroBuilder().entidadeProdutoraNomeBibliotecaApoio("cultural"))).hasSize(1);
    }

    // ---------- correcao critica: joins independentes entre tipos diferentes ----------

    @Test
    void searchByFields_devePermitirCombinarFiltrosDeDoisTiposDeAcervoDiferentesNoMesmoRegistro() {
        TipoDocumento tipoDocumento = umTipoDocumento("Ofício");
        EntidadeProdutora produtora = umaEntidadeProdutora("Secretaria de Estado");
        AcervoDocumental documental = umAcervoDocumental(tipoDocumento, produtora, null);
        AcervoCartografico cartografico = umAcervoCartografico(tipoDocumento, null, "Mapa Raro", "MAPA-RARO");

        // Registro com os DOIS tipos de item — deve bater com filtros dos dois tipos ao mesmo tempo.
        RegistroConsulta comOsDois = registroBase(umPesquisador("Bruno Ferreira", "11111111111"),
                umFuncionario("João da Silva", "111"), LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false);
        RegistroConsultaItem itemDocumental = new RegistroConsultaItem();
        itemDocumental.setAcervoDocumental(documental);
        itemDocumental.setPeriodo("Manhã");
        itemDocumental.setQuantidade(1);
        comOsDois.addItem(itemDocumental);
        RegistroConsultaItem itemCartografico = new RegistroConsultaItem();
        itemCartografico.setAcervoCartografico(cartografico);
        itemCartografico.setPeriodo("Tarde");
        itemCartografico.setQuantidade(1);
        comOsDois.addItem(itemCartografico);
        registroConsultaRepository.save(comOsDois);

        // Registro com SÓ o item documental — nao deve bater quando o filtro cartografico for exigido junto.
        AcervoDocumental outroDocumental = umAcervoDocumental(tipoDocumento, produtora, null);
        umRegistroConsulta(umPesquisador("Maria Santos", "22222222222"), umFuncionario("Ana Souza", "222"),
                LocalDate.of(2026, 8, 19), TipoConsulta.PRESENCIAL, false, outroDocumental);

        // Se os dois tipos compartilhassem o mesmo join, essa combinacao exigiria as duas
        // condicoes na MESMA linha de item (impossivel) e retornaria vazio, mesmo o
        // registro tendo os dois itens em linhas separadas.
        List<RegistroConsulta> resultado = buscar(new FiltroBuilder()
                .entidadeProdutoraNomeDocumental("secretaria")
                .tituloCartografico("raro"));

        assertThat(resultado).extracting(RegistroConsulta::getId).containsExactly(comOsDois.getId());
    }
}
