package com.example.newsfeed.domain.user.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.domain.user.enums.Mbti;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String info;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    public User(String userName, String email, String password, String info, Mbti mbti) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.info = info;
        this.mbti = mbti;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateInfo(String info) {
        this.info = info;
    }

    public void updateMbti(Mbti mbti) {
        this.mbti = mbti;
    }

    public void updatePassword(String encodedPassword) {
        this.password = password;
    }
}
