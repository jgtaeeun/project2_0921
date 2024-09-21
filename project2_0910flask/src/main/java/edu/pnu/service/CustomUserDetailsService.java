package edu.pnu.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.pnu.config.CustomUserDetails;
import edu.pnu.domain.Member;
import edu.pnu.persistence.MemberRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	 private final MemberRepository memberRepository;

	    public CustomUserDetailsService(MemberRepository memberRepository) {
	        this.memberRepository = memberRepository;
	    }

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Member member = memberRepository.findById(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

	        return new CustomUserDetails(
	                member.getUsername(),
	                member.getPassword(),
	                Collections.emptyList(), // 권한 설정 필요
	                member.isRegister(),
	                member.getRole() // Role 설정 필요
	        );
	    }
	}