package com.example.newsfeed.domain.auth.service;

import com.example.newsfeed.common.encoder.PasswordEncoder;
import com.example.newsfeed.common.exception.BaseException;
import com.example.newsfeed.common.exception.ErrorCode;
import com.example.newsfeed.config.JwtUtil;
import com.example.newsfeed.domain.auth.dto.request.LoginRequestDto;
import com.example.newsfeed.domain.auth.dto.request.SignupRequestDto;
import com.example.newsfeed.domain.auth.dto.response.LoginResponseDto;
import com.example.newsfeed.domain.auth.dto.response.SignupResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BaseException(ErrorCode.DUPLICATE_EMAIL, null);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User(dto.getUserName(), dto.getEmail(), encodedPassword, dto.getInfo(), dto.getMbti());
        userRepository.save(user);

        return new SignupResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getInfo(),
                user.getMbti()
        );
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new BaseException(ErrorCode.EMAIL_NOT_FOUND, null)
        );

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD, null);
        }

        String bearJwt = jwtUtil.createToken(user.getId(), user.getEmail());

        return new LoginResponseDto(bearJwt);
    }
}
