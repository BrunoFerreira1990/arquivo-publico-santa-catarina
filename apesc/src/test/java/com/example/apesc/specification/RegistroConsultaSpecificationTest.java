package com.example.apesc.specification;

import com.example.apesc.model.*;
import com.example.apesc.model.enums.*;
import com.example.apesc.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

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
    private AcervoCartograficoRepository acervoCartograficoRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private EntidadeProdutoraRepository entidadeProdutoraRepository;

    @Autowired
    private EntityManager entityManager;

    private TipoDocumento umTipoDocumento() {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNomeDocumento("Ofício");
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    private AcervoDocumental umAcervoDocumental(TipoDocumento tipoDocumento) {
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

    private AcervoCartografico umAcervoCartografico(TipoDocumento tipoDocumento) {
        AcervoCartografico acervo = new AcervoCartografico();
        acervo.setTipoDocumento(tipoDocumento);
        acervo.setCodigoIdentificacao("MAPA-001");
        acervo.setTitulo("Mapa de Santa Catarina");
        acervo.setDimensao("60x80cm");
        acervo.setLocalizacao("Estante B1");
        acervo.setAno("1900");
        acervo.setQuantidadeVolume(1);
        acervo.setDisponibilidade(true);
        return acervoCartograficoRepository.save(acervo);
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

    private RegistroConsulta umRegistroConsulta(Pesquisador pesquisador, Funcionario funcionario,
                                                 LocalDate dataPesquisa, TipoConsulta tipoConsulta,
                                                 Boolean semConsulta, AcervoDocumental acervo) {
        RegistroConsulta registro = new RegistroConsulta();
        registro.setPesquisador(pesquisador);
        registro.setDataPesquisa(dataPesquisa);
        registro.setTipoConsulta(tipoConsulta);
        registro.setFuncionario(funcionario);
        registro.setSemConsulta(semConsulta);

        if (acervo != null) {
            RegistroConsultaItem item = new RegistroConsultaItem();
            item.setAcervoDocumental(acervo);
            item.setPeriodo("Manhã");
            item.setQuantidade(1);
            registro.addItem(item);
        }

        return registroConsultaRepository.save(registro);
    }

    private List<RegistroConsulta> buscar(LocalDate dataPesquisa, String nomePesquisador, String nomeFuncionario,
                                           TipoConsulta tipoConsulta, Boolean semConsulta) {
        Specification<RegistroConsulta> spec = RegistroConsultaSpecification.searchByFields(
                dataPesquisa, nomePesquisador, nomeFuncionario, tipoConsulta, semConsulta
        );
        return registroConsultaRepository.findAll(spec);
    }

    @Test
    void searchByFields_devePesquisarPorNomeDoPesquisadorParcialSemDistinguirMaiusculas() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(umPesquisador("Maria Santos", "22222222222"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(null, "BRUNO", null, null, null);

        assertThat(resultado).extracting(r -> r.getPesquisador().getNome()).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_devePesquisarPorNomeDoFuncionarioParcialSemDistinguirMaiusculas() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");

        umRegistroConsulta(pesquisador, umFuncionario("João da Silva", "111"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, umFuncionario("Ana Souza", "222"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(null, null, "joão", null, null);

        assertThat(resultado).extracting(r -> r.getFuncionario().getNome()).containsExactly("João da Silva");
    }

    @Test
    void searchByFields_devePesquisarPorDataPesquisaExata() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 19), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(LocalDate.of(2026, 8, 18), null, null, null, null);

        assertThat(resultado).extracting(RegistroConsulta::getDataPesquisa).containsExactly(LocalDate.of(2026, 8, 18));
    }

    @Test
    void searchByFields_devePesquisarPorTipoConsulta() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.REMOTO, false, acervo);

        List<RegistroConsulta> resultado = buscar(null, null, null, TipoConsulta.REMOTO, null);

        assertThat(resultado).extracting(RegistroConsulta::getTipoConsulta).containsExactly(TipoConsulta.REMOTO);
    }

    @Test
    void searchByFields_devePesquisarPorSemConsulta() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        Pesquisador pesquisador = umPesquisador("Bruno Ferreira", "11111111111");
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(pesquisador, funcionario, LocalDate.of(2026, 8, 19), TipoConsulta.PRESENCIAL, true, null);

        List<RegistroConsulta> resultado = buscar(null, null, null, null, true);

        assertThat(resultado).extracting(RegistroConsulta::getSemConsulta).containsExactly(true);
        assertThat(resultado.get(0).getItens()).isEmpty();
    }

    @Test
    void searchByFields_deveCombinarMultiplosFiltrosComAnd() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        // mesmo pesquisador (nome parecido), mas tipo de consulta diferente — nao deve bater
        umRegistroConsulta(umPesquisador("Bruno Souza", "22222222222"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.REMOTO, false, acervo);

        List<RegistroConsulta> resultado = buscar(null, "Bruno", null, TipoConsulta.PRESENCIAL, false);

        assertThat(resultado).extracting(r -> r.getPesquisador().getNome()).containsExactly("Bruno Ferreira");
    }

    @Test
    void searchByFields_deveRetornarTodosQuandoNenhumFiltroInformado() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        Funcionario funcionario = umFuncionario("João da Silva", "111");

        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), funcionario,
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);
        umRegistroConsulta(umPesquisador("Maria Santos", "22222222222"), funcionario,
                LocalDate.of(2026, 8, 19), TipoConsulta.REMOTO, false, acervo);

        List<RegistroConsulta> resultado = buscar(null, null, null, null, null);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void searchByFields_deveRetornarVazioQuandoFiltroNaoBateComNenhumRegistro() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), umFuncionario("João da Silva", "111"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(null, "Inexistente", null, null, null);

        assertThat(resultado).isEmpty();
    }

    @Test
    void searchByFields_deveTrazerItensJaCarregadosSemLancarLazyInitializationException() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervo = umAcervoDocumental(tipoDocumento);
        umRegistroConsulta(umPesquisador("Bruno Ferreira", "11111111111"), umFuncionario("João da Silva", "111"),
                LocalDate.of(2026, 8, 18), TipoConsulta.PRESENCIAL, false, acervo);

        List<RegistroConsulta> resultado = buscar(null, null, null, null, null);

        // Simula o fim da transacao/sessao (open-in-view=false): limpa o contexto de
        // persistencia depois da query, antes de acessar a colecao "itens". Se o fetch
        // da specification nao estivesse la, isso lancaria LazyInitializationException.
        entityManager.clear();

        assertThatCode(() -> resultado.forEach(r -> r.getItens().size())).doesNotThrowAnyException();
        assertThat(resultado.get(0).getItens()).hasSize(1);
    }

    @Test
    void searchByFields_naoDeveDuplicarRegistroComMaisDeUmItem() {
        TipoDocumento tipoDocumento = umTipoDocumento();
        AcervoDocumental acervoDocumental = umAcervoDocumental(tipoDocumento);
        AcervoCartografico acervoCartografico = umAcervoCartografico(tipoDocumento);

        RegistroConsulta registro = new RegistroConsulta();
        registro.setPesquisador(umPesquisador("Bruno Ferreira", "11111111111"));
        registro.setFuncionario(umFuncionario("João da Silva", "111"));
        registro.setDataPesquisa(LocalDate.of(2026, 8, 18));
        registro.setTipoConsulta(TipoConsulta.PRESENCIAL);
        registro.setSemConsulta(false);

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
        List<RegistroConsulta> resultado = buscar(null, null, null, null, null);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getItens()).hasSize(2);
    }
}
