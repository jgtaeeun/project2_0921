package edu.pnu.config;

import java.util.Map;

import javax.swing.Spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import edu.pnu.config.filter.JWTAuthenticationFilter;
import edu.pnu.config.filter.JWTAuthorizationFilter;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.service.CustomUserDetailsService;





@Configuration
@EnableWebSecurity
public class SecurityConfig {

	   private final CustomUserDetailsService userDetailsService;
	    private final AuthenticationConfiguration authenticationConfiguration;
	    private final MemberRepository memberRepository;

	    
	    private final CorsConfigurationSource corsConfigurationSource;

	   
	    public SecurityConfig(CustomUserDetailsService userDetailsService,
	                          AuthenticationConfiguration authenticationConfiguration,
	                          MemberRepository memberRepository,
	                          CorsConfigurationSource corsConfigurationSource) {
	        this.userDetailsService = userDetailsService;
	        this.authenticationConfiguration = authenticationConfiguration;
	        this.memberRepository = memberRepository;
	        this.corsConfigurationSource = corsConfigurationSource;
	    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Create JWTAuthenticationFilter instance manually
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager(), userDetailsService);
        JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter(memberRepository,userDetailsService);
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors ->cors.configurationSource(corsSource()))// CustomConfig에서 CORS 설정을 제공
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/signup").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/images/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/member/**").hasAnyRole("MEMBER","ADMIN")
                .requestMatchers("/board/**").authenticated()
                .anyRequest().permitAll())
            .formLogin(frmLogin -> frmLogin.disable())
            .httpBasic(basic -> basic.disable())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                    .accessDeniedPage("/accessDenied"))
            .logout(logout -> logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login"))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public  CorsConfigurationSource corsSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://localhost:3000");
        configuration.addAllowedOriginPattern("http://192.168.0.133:3000");
        configuration.addAllowedMethod(CorsConfiguration.ALL);
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.addExposedHeader(HttpHeaders.AUTHORIZATION);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}



