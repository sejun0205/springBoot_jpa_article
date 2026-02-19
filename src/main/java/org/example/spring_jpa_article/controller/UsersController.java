package org.example.spring_jpa_article.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.spring_jpa_article.domain.Users;
import org.example.spring_jpa_article.dto.LoginRequestDto;
import org.example.spring_jpa_article.dto.SignUpRequestDto;
import org.example.spring_jpa_article.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;




    //회원 페이지
    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    //회원가입 처리
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> signup(@RequestBody SignUpRequestDto req){
        Map<String,Object> result = new HashMap<>();

        try {
            userService.signUp(req);
            result.put("success", true);
            result.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IllegalStateException e){
            result.put("success",false);
            result.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }catch (Exception e){
            e.printStackTrace(); // 서버 콘솔에서 정확한 원인 확인
            result.put("success", false);
            result.put("message", "서버 오류: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    //로그인 페이지
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    //로그인 처리(세션저장)
    @PostMapping("login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto req, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            Users user = userService.login(req);

            //  세션에 로그인 정보 저장
            session.setAttribute("LOGIN_USER_BNO", user.getUserBno());
            session.setAttribute("LOGIN_USER_ID", user.getUserId());

            result.put("success", true);
            result.put("message", "로그인 성공");
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(result);
        }
    }

    //로그아웃
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> logout(HttpSession session){
        session.invalidate();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "로그아웃 완료");
        return ResponseEntity.ok(result);
    }

}
