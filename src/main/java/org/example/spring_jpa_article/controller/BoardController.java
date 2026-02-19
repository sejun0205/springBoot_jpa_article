package org.example.spring_jpa_article.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.spring_jpa_article.domain.Board;
import org.example.spring_jpa_article.dto.BoardCreateRequestDto;
import org.example.spring_jpa_article.dto.BoardUpdateRequestDto;
import org.example.spring_jpa_article.service.BoardService;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    //글쓰기 페이지
    @GetMapping("/new")
    public String newPage(HttpSession session){
        Long loginUserBno = (Long) session.getAttribute("LOGIN_USER_BNO");
        if(loginUserBno == null) return "redirect:/users/login";
        return "new";
    }


    //글 등록
    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> create(@RequestBody BoardCreateRequestDto req,
                                                      HttpSession session){
        Map<String,Object> result = new HashMap<>();

        try{
            Long loginUserBno = (Long) session.getAttribute("LOGIN_USER_BNO");
            Long boardId = boardService.create(req, loginUserBno);

            result.put("success", true);
            result.put("message", "글 등록 완료");
            result.put("boardId", boardId);
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException | IllegalStateException e){
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @GetMapping("/list")
    public String listPage(Model model){
        List<Board> boards = boardService.findAllLatest();
        model.addAttribute("boards",boards);
        return "list";
    }

    @GetMapping("/{boardBno}")
    public String detailPage(@PathVariable("boardBno")Long boardBno,HttpSession session, Model model){
        Board board = boardService.findById(boardBno);
        model.addAttribute("board",board);

        Long loginUserBno = (Long) session.getAttribute("LOGIN_USER_BNO");
        boolean isOwner = false;

        if(loginUserBno != null && board.getUserBno() != null && board.getUserBno().getUserBno() != null){
            isOwner = loginUserBno.equals(board.getUserBno().getUserBno());
        }
        model.addAttribute("isOwner", isOwner);
        return "detail";
    }


    //수정 페이지
    @GetMapping("/{boardBno}/edit")
    public String editPage(@PathVariable("boardBno") Long boardBno, HttpSession session,
                           Model model){
        Long loginUserBno = (Long) session.getAttribute("LOGIN_USER_BNO");
        if(loginUserBno == null) return "redirect:/users/login";

        Board board = boardService.findById(boardBno);

        //작성자만 접근 가능
        if(!board.getUserBno().getUserBno().equals(loginUserBno)){
            return "redirect:/boards/"+boardBno;
        }
        model.addAttribute("board", board);
        return "edit";
    }

    //수정 처리
    @PutMapping("/{boardBno}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> update(@PathVariable("boardBno")Long boardBno,
                                                     @RequestBody BoardUpdateRequestDto req,
                                                     HttpSession session){
        Map<String,Object> result = new HashMap<>();
        try{
            Long loginUserBno = (Long) session.getAttribute("LOGIN_USER_BNO");
            boardService.update(boardBno,req,loginUserBno);

            result.put("success",true);
            result.put("message","수정 완료");
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException | IllegalStateException e){
            result.put("success",false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "서버 오류");
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @DeleteMapping("/{boardBno}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> delete(@PathVariable("boardBno")Long boardBno,
                                                      HttpSession session){
        Map<String, Object> result = new HashMap<>();
        try{
            Long loginUserBno = (Long) session.getAttribute("LOGIN_USER_BNO");
            boardService.delete(boardBno,loginUserBno);

            result.put("success", true);
            result.put("message", "삭제완료");
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException | IllegalStateException e){
            result.put("success", false);
            result.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message","서버 오류");
            return ResponseEntity.internalServerError().body(result);
        }
    }
}
