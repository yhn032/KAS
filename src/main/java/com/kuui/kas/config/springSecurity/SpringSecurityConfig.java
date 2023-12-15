package com.kuui.kas.config.springSecurity;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        request -> request.requestMatchers(PathRequest.toH2Console()).permitAll()
                                .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()));

        return http.build();
    }
}
