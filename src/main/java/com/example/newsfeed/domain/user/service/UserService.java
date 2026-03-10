package com.example.newsfeed.domain.user.service;

import com.example.newsfeed.common.encoder.PasswordEncoder;
import com.example.newsfeed.common.exception.BaseException;
import com.example.newsfeed.common.exception.ErrorCode;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.user.dto.request.ChangePasswordRequestDto;
import com.example.newsfeed.domain.user.dto.request.UserUpdateRequestDto;
import com.example.newsfeed.domain.user.dto.response.UserResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 전체 조회(페이징)
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> dtoList = userPage.getContent().stream()
                .map(UserResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, userPage.getTotalElements());
    }

    // 유저 단일 조회
    @Transactional(readOnly = true)
    public UserResponseDto getUser(AuthUser authUser, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        return UserResponseDto.toDto(user);
    }

    // 본인 정보 수정
    @Transactional
    public UserResponseDto updateMyProfile(AuthUser authUser, UserUpdateRequestDto dto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        if(dto.getUserName() != null) {
            user.updateUserName(dto.getUserName());
        }

        if(dto.getEmail() != null) {
            user.updateEmail(dto.getEmail());
        }

        if(dto.getInfo() != null) {
            user.updateInfo(dto.getInfo());
        }

        if(dto.getMbti() != null) {
            user.updateMbti(dto.getMbti());
        }

        return UserResponseDto.toDto(user);

    }

    @Transactional
    public void changePassword(AuthUser authUser, @Valid ChangePasswordRequestDto dto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        // 비밀번호 수정시, 본인 확인 (비밀번호 일치 여부 확인)
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD, null);
        }

        // 현재 비밀번호와 새 비밀번호가 같은지 확인
        if (!dto.getOldPassword().equals(dto.getNewPassword())) {
            throw new BaseException(ErrorCode.SAME_AS_OLD_PASSWORD, null);
        }

        // 새 비밀번호와 새 비밀번호 확인 번호가 일치하는지 확인
        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            throw new BaseException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH, null);
        }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        user.updatePassword(encodedPassword);
    }

    @Transactional
    public void deleteUser(AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new BaseException(ErrorCode.USER_NOT_FOUND, null)
        );

        userRepository.delete(user);
    }
}
