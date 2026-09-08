package com.example.apesc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String tipo;
    private long expiraEmSegundos;
    private Long funcionarioId;
    private String nome;
    private String email;
    private List<String> autoridades;
}
