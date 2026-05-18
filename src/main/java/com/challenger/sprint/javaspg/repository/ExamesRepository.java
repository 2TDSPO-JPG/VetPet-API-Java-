package com.challenger.sprint.javaspg.repository;

import com.challenger.sprint.javaspg.entity.Exames;
import com.challenger.sprint.javaspg.enuns.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ExamesRepository extends JpaRepository<Exames, Long> {
    Page<Exames> findExamesByPetCode(String petCode, Pageable pageable);
    Page<Exames> findByDataExameBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
    Page<Exames> findExamesByStatus(Status status, Pageable pageable);
    Page<Exames> findExamesByNomeDoProfissional(String nomeDoProfissional, Pageable pageable);
    Page<Exames> findExamesByEmailTutores(String emailTutores, Pageable pageable);
    Optional<Exames> findFirstByPetCodeAndDataExameAfterOrderByDataExameAsc(String petCode, LocalDateTime dataAtual);
}
