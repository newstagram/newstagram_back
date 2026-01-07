package com.newstagram.api.users.model.service;

import com.newstagram.api.users.model.dto.CustomUserDetails;
import com.newstagram.api.users.repository.UserRepository;
import com.newstagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("유저 없음: " + email)
        );
        return new CustomUserDetails(userData);
    }
}
