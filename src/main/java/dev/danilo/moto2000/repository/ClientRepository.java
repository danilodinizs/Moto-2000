package dev.danilo.moto2000.repository;

import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByCpf(String cpf);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhoneNumber(String phoneNumber);
    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    
}
