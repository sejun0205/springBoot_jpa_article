package org.example.spring_jpa_article.service;

import org.example.spring_jpa_article.domain.Board;
import org.example.spring_jpa_article.dto.BoardCreateRequestDto;
import org.example.spring_jpa_article.dto.BoardUpdateRequestDto;

import java.util.List;

public interface BoardService {
    Long create(BoardCreateRequestDto req, Long loginUserBno);
    List<Board> findAllLatest();
    Board findById(Long boardBno);
    Long update(Long boardBno, BoardUpdateRequestDto req, Long loginUserBno);
    void delete(Long boardBno, Long loginUserBno);
}
