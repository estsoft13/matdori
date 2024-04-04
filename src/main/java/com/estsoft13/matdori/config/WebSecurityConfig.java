package com.estsoft13.matdori.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 배포시 주석처리
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    @Bean
    public WebSecurityCustomizer configure() { // 스프링시큐리티 비활성화
        return web -> web.ignoring().requestMatchers(toH2Console())
                .requestMatchers("/static/**","/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html"
                        ,"/error","/js/**", "/css/**", "/images/**","/admin/new");
    }

    /* 배포시 사용할 코드(위 메소드 주석처리)
    @Bean
    public WebSecurityCustomizer configure() { // 스프링시큐리티 비활성화

        return (web) -> web.ignoring().requestMatchers("/static/**","/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html"
                        ,"/error","/js/**", "/css/**", "/images/**", "/admin/new");
    }
    */


    // 특정 http 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/files/**","/","/css/**", "/login", "/signup", "/forgot","/user", "/admin/new", "/reviews").permitAll()
                                .requestMatchers("/admin/manage", "/restaurants").hasRole("ADMIN")
                                .requestMatchers("/meetings").hasAnyRole("ASSOCIATE","MEMBER","ADMIN")
                                .requestMatchers("/meeting/**").hasAnyRole("MEMBER","ADMIN")
                                .anyRequest().authenticated())
                .formLogin(auth -> auth.loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/reviews"))
                .logout(auth -> auth.logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .csrf(auth -> auth.disable());
        return httpSecurity.build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}