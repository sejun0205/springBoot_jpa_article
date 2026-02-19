package org.example.spring_jpa_article.repository;

import org.example.spring_jpa_article.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // 회원가입 시 아이디 중복 체크
    boolean existsByUserId(String userId);

    //로그인 구현
    Optional<Users> findByUserId(String userId);


}
