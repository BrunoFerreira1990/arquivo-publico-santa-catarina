package com.example.apesc.service.auth;

import com.example.apesc.dto.LoginRequestDTO;
import com.example.apesc.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);
}
