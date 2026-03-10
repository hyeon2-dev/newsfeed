package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.user.dto.request.ChangePasswordRequestDto;
import com.example.newsfeed.domain.user.dto.request.UserUpdateRequestDto;
import com.example.newsfeed.domain.user.dto.response.UserResponseDto;
import com.example.newsfeed.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 전체 조회(페이징)
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    // 유저 단일 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUser(
            @Auth AuthUser authUser,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.getUser(authUser, userId));
    }

    // 본인 정보 수정
    @PutMapping("/users/my-profile")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @Auth AuthUser authUser,
            @RequestBody @Valid UserUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(userService.updateMyProfile(authUser, dto));
    }

    // 본인 비밀번호 수정
    @PutMapping("/users/change-password")
    public ResponseEntity<String> changePassword(
            @Auth AuthUser authUser,
            @Valid @RequestBody ChangePasswordRequestDto dto
    ) {
        userService.changePassword(authUser, dto);
        return new ResponseEntity<>("패스워드가 변경됐습니다.", HttpStatus.OK);
    }

    // 회원 탈퇴
    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(@Auth AuthUser authUser) {
        userService.deleteUser(authUser);
        return new ResponseEntity<>("유저가 삭제되었습니다.", HttpStatus.OK);
    }

}
