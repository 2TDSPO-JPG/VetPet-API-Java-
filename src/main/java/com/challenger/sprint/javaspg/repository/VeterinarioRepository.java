package com.challenger.sprint.javaspg.repository;

import com.challenger.sprint.javaspg.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    Optional<Veterinario> findByCrmv(String crmv);
    Optional<Veterinario> findByEmail(String email);
}