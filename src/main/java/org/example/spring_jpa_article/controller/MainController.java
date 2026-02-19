package org.example.spring_jpa_article.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    @GetMapping("/")
    public String main(HttpSession session, Model model){
        String loginUserId = (String) session.getAttribute("LOGIN_USER_ID");
        model.addAttribute("loginUserId", loginUserId);
        return "home";
    }

}
