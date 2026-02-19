package org.example.spring_jpa_article.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.example.spring_jpa_article.domain.Users;
import org.example.spring_jpa_article.dto.LoginRequestDto;
import org.example.spring_jpa_article.dto.SignUpRequestDto;
import org.example.spring_jpa_article.repository.UsersRepository;
import org.example.spring_jpa_article.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    //기본 검증
    @Override
    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if(signUpRequestDto == null){
            throw  new IllegalArgumentException("요청 값이 없습니다.");
        }

        String userId = signUpRequestDto.getUserId() != null ? signUpRequestDto.getUserId().trim() : "";
        String userPwd = signUpRequestDto.getUserPwd() != null ? signUpRequestDto.getUserPwd().trim() : "";

        if (userId.isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (userPwd.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }


        //아이디 중복 체크
        if(usersRepository.existsByUserId(userId)){
            throw new IllegalStateException("이미 사용중인 아이디 입니다.");
        }

        //비밀번호 암호화
        String encodePwd = passwordEncoder.encode(userPwd);

        //저장
        Users users = Users.builder()
                .userId(userId)
                .userPwd(encodePwd)
                .build();

        usersRepository.save(users);

    }

    @Override
    public Users login(LoginRequestDto req) {
        String userId = req.getUserId() == null ? "" : req.getUserId().trim();
        String userPwd = req.getUserPwd() == null ? "" : req.getUserPwd().trim();

        if(userId.isEmpty()) throw new IllegalArgumentException("아이디를 입력해 주세요");
        if(userPwd.isEmpty()) throw new IllegalArgumentException("비밀번호를 입력해 주세요");

        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(userPwd, user.getUserPwd())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다");
        }
        return user;
    }

}
