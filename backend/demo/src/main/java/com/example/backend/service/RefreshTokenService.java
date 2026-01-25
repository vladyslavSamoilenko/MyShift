package com.example.backend.service;

import com.example.backend.model.entities.RefreshToken;
import com.example.backend.model.entities.User;
import com.example.backend.repository.RefreshTokenRepository;

public interface RefreshTokenService {
    RefreshToken generateOrUpdateRefreshToken(User user);

    RefreshToken validateAndRefreshToken(String refreshToken);
}
