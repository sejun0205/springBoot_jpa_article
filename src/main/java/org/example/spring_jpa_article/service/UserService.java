package org.example.spring_jpa_article.service;

import org.example.spring_jpa_article.domain.Users;
import org.example.spring_jpa_article.dto.LoginRequestDto;
import org.example.spring_jpa_article.dto.SignUpRequestDto;

public interface UserService {

    //회원가입
    void signUp(SignUpRequestDto signUpRequestDto);

    //로그인 성공 시 userBno 반환
    Users login(LoginRequestDto req);

}
