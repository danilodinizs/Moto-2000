package dev.danilo.moto2000.repository;

import dev.danilo.moto2000.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    //UserDetails findByUsername(String username);
}
