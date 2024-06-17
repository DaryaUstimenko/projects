package com.example.newsservice.security;

import com.example.newsservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private String currentUserId;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        currentUserId = userService.findByUsername(username).getId().toString();
        return new AppUserPrincipal(userService.findByUsername(username));
    }

    public String getUserId(){
        return currentUserId;
    }
}
