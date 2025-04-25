package dev.danilo.moto2000.repository;

import dev.danilo.moto2000.entity.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {
}
