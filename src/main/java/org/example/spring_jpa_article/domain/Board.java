package org.example.spring_jpa_article.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "board", schema = "ajax_article")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_bno")
    private Long boardBno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_bno")
    private Users userBno;

    @Column(name = "board_title", nullable = false, length = 50)
    private String boardTitle;

    @Column(name = "board_content", nullable = false,columnDefinition = "TEXT")
    private String boardContent;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public void change(String title, String content){
        this.boardTitle = title;
        this.boardContent = content;
    }
}
