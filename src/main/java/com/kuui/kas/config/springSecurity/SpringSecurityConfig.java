package com.kuui.kas.config.springSecurity;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain exceptionSecurityFilterChain(HttpSecurity http) throws  Exception {
        http
                .authorizeRequests()
                    .antMatchers("/common/login", "/common/signup", "/loginProc", "/logout").permitAll() //시큐리티에서 자동제공하는 로그인/아웃 과정은 모두 허용
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/common/login")
                    .loginProcessingUrl("/loginProc")
                    .usernameParameter("id")
                    .passwordParameter("pw")
                    .defaultSuccessUrl("/common/dashboard", true)
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
                    .ignoringAntMatchers("/common/signup", "/loginProc", "/logout");

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requestMatchers((matchers) -> matchers.antMatchers("/static/**"))
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
