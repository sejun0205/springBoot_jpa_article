package org.example.spring_jpa_article.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "ajax_article")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_bno")
    private Long userBno;

    @Column(name = "user_id", nullable = false, length = 45)
    private String userId;

    @Column(name = "user_pwd", nullable = false, length = 100)
    private String userPwd;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
