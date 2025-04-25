package dev.danilo.moto2000.config;

import dev.danilo.moto2000.entity.User;
import dev.danilo.moto2000.enums.UserRole;
import dev.danilo.moto2000.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Moto2000*"));
            admin.setRole(UserRole.MANAGER);
            repository.save(admin);
        }
    }
}
