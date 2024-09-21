package edu.pnu.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import edu.pnu.domain.Member;
import edu.pnu.domain.Role;
import edu.pnu.service.MemberService;

import lombok.RequiredArgsConstructor;


@RestController
public class LoginController {

	

    @Autowired
    private PasswordEncoder passwordEncoder; // 주입

    @Autowired
    private MemberService memberService;
    
    
    @PostMapping("/login/signup")
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
    	 // Encrypt the password
    	
    	member.setPhonenumber(member.getPassword());
          
    	  if (member.getPassword() != null) {
              String encodedPassword = passwordEncoder.encode(member.getPassword());
              member.setPassword(encodedPassword);
          }
      	
 
    	
        if (member.getRole() == null) {
            member.setRole(Role.ROLE_MEMBER); // 기본값 설정
        }
        
        Member addedMember = memberService.addMember(member);
        if (addedMember != null) {
            return ResponseEntity.status(201).body(addedMember);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
 
    
 
   
    
   
}