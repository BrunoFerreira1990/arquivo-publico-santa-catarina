package com.example.apesc.security;

import com.example.apesc.model.Funcionario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adapta um {@link Funcionario} ao contrato do Spring Security usado apenas
 * durante o login (validação de e-mail + senha pelo {@code AuthenticationManager}).
 * A autoridade é derivada da regra de permissão vinculada ao funcionário.
 */
public class FuncionarioUserDetails implements UserDetails {

    private final Funcionario funcionario;

    public FuncionarioUserDetails(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    /** Converte o nome da regra (ex.: "Administrador Geral") na authority "ROLE_ADMINISTRADOR_GERAL". */
    public static String toAuthority(String nomeRegra) {
        return "ROLE_" + nomeRegra.trim().toUpperCase().replaceAll("\\s+", "_");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (funcionario.getPermissoes() == null || funcionario.getPermissoes().getNomeRegra() == null
                || funcionario.getPermissoes().getNomeRegra().trim().isEmpty()) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(toAuthority(funcionario.getPermissoes().getNomeRegra())));
    }

    @Override
    public String getPassword() {
        return funcionario.getSenha();
    }

    @Override
    public String getUsername() {
        return funcionario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
