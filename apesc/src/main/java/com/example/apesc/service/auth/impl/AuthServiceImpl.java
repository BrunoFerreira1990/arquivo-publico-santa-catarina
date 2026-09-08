package com.example.apesc.service.auth.impl;

import com.example.apesc.dto.LoginRequestDTO;
import com.example.apesc.dto.LoginResponseDTO;
import com.example.apesc.exception.CustomException;
import com.example.apesc.exception.ErrorConstants;
import com.example.apesc.security.FuncionarioUserDetails;
import com.example.apesc.security.JwtService;
import com.example.apesc.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail() == null ? null : request.getEmail().trim(),
                    request.getSenha()
                )
            );
        } catch (AuthenticationException ex) {
            // Mensagem genérica de propósito: não revelar se o e-mail existe.
            throw new CustomException(ErrorConstants.CREDENCIAIS_INVALIDAS, HttpStatus.UNAUTHORIZED);
        }

        FuncionarioUserDetails userDetails = (FuncionarioUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        List<String> autoridades = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return new LoginResponseDTO(
            token,
            "Bearer",
            jwtService.getExpirationSeconds(),
            userDetails.getFuncionario().getId(),
            userDetails.getFuncionario().getNome(),
            userDetails.getFuncionario().getEmail(),
            autoridades
        );
    }
}
