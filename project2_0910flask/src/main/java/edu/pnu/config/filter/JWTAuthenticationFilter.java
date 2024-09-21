package edu.pnu.config.filter;

import java.io.IOException;
import java.util.Date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pnu.config.CustomUserDetails;
import edu.pnu.domain.Member;
import edu.pnu.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;







public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Member member = mapper.readValue(request.getInputStream(), Member.class);
            System.out.println( member);
            Authentication authToken = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
            System.out.println( authToken);
            Authentication authentication = authenticationManager.authenticate(authToken);
            System.out.println( authentication);
            
            // Check if the user is registered
            UserDetails principal = userDetailsService.loadUserByUsername(member.getUsername());
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) principal;
                if (!customUserDetails.isRegister()) {
                    throw new AuthenticationException("User is not registered") {};
                }
            } else {
                throw new AuthenticationException("UserDetails is not an instance of CustomUserDetails") {};
            }

            return authentication;
        } catch (UsernameNotFoundException e) {
            log.info("User not found: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        } catch (Exception e) {
            log.info("Authentication failed: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
    	  if (authResult.getPrincipal() instanceof CustomUserDetails) {
    	        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

            // JWT 토큰 생성
            String token = JWT.create()
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60  * 60)) // 30분 만료
                    .withClaim("username", userDetails.getUsername())
                    .sign(Algorithm.HMAC256("edu.pnu.jwt")); // 보안 키

            // 응답 객체 생성
            AuthResponse authResponse = new AuthResponse(token, userDetails);

            // 응답 설정
            response.setContentType("application/json");
            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            response.setStatus(HttpServletResponse.SC_OK);

            // JSON 변환 및 응답 작성
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), authResponse);
        } else {
            throw new ServletException("Authentication principal is not of type CustomUserDetails");
        }
    }

}