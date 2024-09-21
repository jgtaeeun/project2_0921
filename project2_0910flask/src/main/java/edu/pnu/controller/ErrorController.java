package edu.pnu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    
	@GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied"; // 템플릿 이름을 반환합니다
    }
}
