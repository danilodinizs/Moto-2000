package dev.danilo.moto2000.security;

import dev.danilo.moto2000.entity.User;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Username not found"));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
