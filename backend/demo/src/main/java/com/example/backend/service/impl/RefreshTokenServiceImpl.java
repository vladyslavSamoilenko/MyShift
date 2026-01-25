package com.example.backend.service.impl;

import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.entities.RefreshToken;
import com.example.backend.model.entities.User;
import com.example.backend.model.exception.NotFoundException;
import com.example.backend.repository.RefreshTokenRepository;
import com.example.backend.service.RefreshTokenService;
import com.example.backend.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public RefreshToken generateOrUpdateRefreshToken(User user) {
        return refreshTokenRepository.findByUserId(user.getId())
                .map(refreshToken -> {
                    refreshToken.setCreated(LocalDateTime.now());
                    refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
                    return refreshTokenRepository.save(refreshToken);
                }).orElseGet(() -> {
                    RefreshToken newToken = new RefreshToken();
                    newToken.setUser(user);
                    newToken.setCreated(LocalDateTime.now());
                    newToken.setToken(ApiUtils.generateUuidWithoutDash());
                    return refreshTokenRepository.save(newToken);
                });
    }

    @Override
    public RefreshToken validateAndRefreshToken(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_REFRESH_TOKEN.getMessage()));

        refreshToken.setCreated(LocalDateTime.now());
        refreshToken.setToken(ApiUtils.generateUuidWithoutDash());
        return refreshTokenRepository.save(refreshToken);
    }
}
