package com.example.apesc.controller;

import com.example.apesc.dto.LoginRequestDTO;
import com.example.apesc.dto.LoginResponseDTO;
import com.example.apesc.dto.MeResponseDTO;
import com.example.apesc.security.AuthenticatedUser;
import com.example.apesc.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> me(
        @AuthenticationPrincipal AuthenticatedUser user,
        Authentication authentication
    ) {
        List<String> autoridades = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        return ResponseEntity.ok(new MeResponseDTO(user.id(), user.nome(), user.email(), autoridades));
    }
}
