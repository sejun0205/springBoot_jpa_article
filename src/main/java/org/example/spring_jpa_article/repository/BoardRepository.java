package org.example.spring_jpa_article.repository;

import org.example.spring_jpa_article.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByBoardBnoDesc();
}
