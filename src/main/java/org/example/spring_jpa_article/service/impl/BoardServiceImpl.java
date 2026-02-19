package org.example.spring_jpa_article.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.spring_jpa_article.domain.Board;
import org.example.spring_jpa_article.domain.Users;
import org.example.spring_jpa_article.dto.BoardCreateRequestDto;
import org.example.spring_jpa_article.dto.BoardUpdateRequestDto;
import org.example.spring_jpa_article.repository.BoardRepository;
import org.example.spring_jpa_article.repository.UsersRepository;
import org.example.spring_jpa_article.service.BoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private  final UsersRepository usersRepository;

    @Override
    public Long create(BoardCreateRequestDto req, Long loginUserBno) {
        if(loginUserBno == null){
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        String title = req.getBoardTitle() == null ? "" : req.getBoardTitle().trim();
        String content = req.getBoardContent() == null ? "" : req.getBoardContent().trim();

        if(title.isEmpty()) throw  new IllegalArgumentException("제목을 입력하세요");
        if(content.isEmpty()) throw new IllegalArgumentException("내용을 입력하세요");

        Users writer = usersRepository.findById(loginUserBno)

                .orElseThrow(()-> new IllegalStateException("로그인 사용자 정보가 없습니다."));

        Board board = Board.builder()
                .boardTitle(title)
                .boardContent(content)
                .userBno(writer)
                .build();

        Board saved = boardRepository.save(board);
        return saved.getBoardBno();
    }

    @Override
    public List<Board> findAllLatest() {
        return boardRepository.findAllByOrderByBoardBnoDesc();
    }

    @Override
    public Board findById(Long boardBno) {
        return boardRepository.findById(boardBno)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    @Override
    @Transactional
    public Long update(Long boardBno, BoardUpdateRequestDto req, Long loginUserBno) {
        if(loginUserBno == null){
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Board board = boardRepository.findById(boardBno)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시판입니다."));

        //작성자만 수정 가능
        Long writerBno = board.getUserBno().getUserBno();
        if(!writerBno.equals(loginUserBno)){
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        String title = req.getBoardTitle() == null ? "" : req.getBoardTitle().trim();
        String content = req.getBoardContent() == null ? "" : req.getBoardContent().trim();

        if(title.isEmpty()) throw new IllegalArgumentException("제목을 입력하세요.");
        if(content.isEmpty()) throw new IllegalArgumentException("내용을 입력하세요.");

        board.change(title, content);

        return board.getBoardBno();
    }

    @Override
    @Transactional
    public void delete(Long boardBno, Long loginUserBno) {
        if(loginUserBno == null){
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Board board = boardRepository.findById(boardBno)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시물입니다."));

        //작성자만 삭제 가능
        Long writerBno = board.getUserBno().getUserBno();
        if(!writerBno.equals(loginUserBno)){
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
        boardRepository.delete(board);
    }
}
