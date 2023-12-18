package com.kuui.kas.application.teacher.service;

import com.kuui.kas.application.teacher.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = new Account();
        account.setId(username);

        return null;
    }
    //Spring Security에서 제공해주는 url 호출없이 로그인 로직을 처리할 수 있게 함
}
