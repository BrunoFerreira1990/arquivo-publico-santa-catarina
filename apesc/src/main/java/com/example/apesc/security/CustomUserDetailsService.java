package com.example.apesc.security;

import com.example.apesc.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final FuncionarioRepository funcionarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return funcionarioRepository.findByEmailIgnoreCase(email)
            .filter(f -> f.getSenha() != null && !f.getSenha().isEmpty())
            .map(FuncionarioUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException("Funcionário não encontrado: " + email));
    }
}
