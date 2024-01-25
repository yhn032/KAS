package com.kuui.kas.config.springSecurity;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain exceptionSecurityFilterChain(HttpSecurity http) throws  Exception {
        http
                .authorizeRequests()
                    .antMatchers("/teacher/duplicateId", "/common/login", "/common/signup", "/loginProc", "/logout", "/img/**", "/css/**", "/js/**").permitAll() //시큐리티에서 자동제공하는 로그인/아웃 과정은 모두 허용
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/common/login")         //사용자 정의 로그인 페이지
                    .defaultSuccessUrl("/common/dashboard", true) //로그인 성공 후 이동 페이지
                    .failureUrl("/common/login?error=true")             //로그인 실패 후 이동 페이지
                    .usernameParameter("id")
                    .passwordParameter("pw")
                    .loginProcessingUrl("/loginProc")   //로그인 폼 action url
                    .successHandler(loginSuccessHandler())      //해당 핸들러를 생성하여 결과에 따라 핸들링 해준다.
                    .failureHandler(loginFailureHandler())
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/common/login")
                    .deleteCookies("JSESSIONID","remember-me")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                .and()
                    .csrf()//개발 단계에서는 csrf토큰이 없기에 막아두지만, 실제 운영 상황에서는 공격을 방지하지 위해 필터링 및 토큰이 필요하다
                    .ignoringAntMatchers("/asset/addList","/teacher/duplicateId","/common/signup", "/loginProc", "/logout");

        return http.build();
    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        AuthenticationSuccessHandler handler = (request, response, authentication) -> {
            System.out.println("authentication :: " + authentication.getName());
            response.sendRedirect("/common/dashboard");
        };
        return handler;
    }

    private AuthenticationFailureHandler loginFailureHandler() {
        AuthenticationFailureHandler handler = ((request, response, exception) -> {
            System.out.println("exception :: " + exception.getMessage());
            response.sendRedirect("/common/login");
        });
        return handler;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requestMatchers((matchers) -> matchers.antMatchers("/static/css","/static/js","/static/img"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache().disable()
                .securityContext().disable()
                .sessionManagement().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
