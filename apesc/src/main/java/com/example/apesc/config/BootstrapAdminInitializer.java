package com.example.apesc.config;

import com.example.apesc.model.Funcionario;
import com.example.apesc.model.Permissoes;
import com.example.apesc.model.enums.Generos;
import com.example.apesc.repository.FuncionarioRepository;
import com.example.apesc.repository.PermissoesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Resolve o problema do "primeiro acesso": como todos os endpoints exigem
 * autenticação, sem um funcionário inicial ninguém consegue logar para cadastrar
 * o primeiro. Cria um administrador padrão apenas quando ainda não existe nenhum
 * funcionário com senha. Trocar a senha (ou desativar via APP_BOOTSTRAP_ADMIN_ENABLED=false)
 * é responsabilidade da operação.
 */
@Component
@RequiredArgsConstructor
public class BootstrapAdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BootstrapAdminInitializer.class);
    private static final String REGRA_ADMIN = "ADMINISTRADOR";

    private final FuncionarioRepository funcionarioRepository;
    private final PermissoesRepository permissoesRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap-admin.enabled:true}")
    private boolean enabled;

    @Value("${app.bootstrap-admin.email:admin@apesc.local}")
    private String email;

    @Value("${app.bootstrap-admin.senha:admin12345}")
    private String senha;

    @Value("${app.bootstrap-admin.nome:Administrador APESC}")
    private String nome;

    @Override
    @Transactional
    public void run(String... args) {
        if (!enabled || funcionarioRepository.existsBySenhaIsNotNull()) {
            return;
        }

        Permissoes admin = permissoesRepository.findByNomeRegraIgnoreCase(REGRA_ADMIN).stream()
            .findFirst()
            .orElseGet(() -> {
                Permissoes nova = new Permissoes();
                nova.setNomeRegra(REGRA_ADMIN);
                return permissoesRepository.save(nova);
            });

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nome);
        funcionario.setEmail(email);
        funcionario.setSenha(passwordEncoder.encode(senha));
        funcionario.setDataNascimento(LocalDate.of(1970, 1, 1));
        funcionario.setGenero(Generos.MASCULINO);
        funcionario.setNumeroMatricula("ADMIN-0001");
        funcionario.setCargo("Administrador do sistema");
        funcionario.setSetor("Tecnologia da Informação");
        funcionario.setPermissoes(admin);

        funcionarioRepository.save(funcionario);

        log.warn("Funcionário administrador inicial criado ({}). Altere a senha padrão imediatamente.", email);
    }
}
