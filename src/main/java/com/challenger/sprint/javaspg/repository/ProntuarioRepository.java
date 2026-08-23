package com.challenger.sprint.javaspg.repository;

import com.challenger.sprint.javaspg.entity.Prontuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    List<Prontuario> findByExameId(Long exameId);
    List<Prontuario> findByPetId(Long petId);
    List<Prontuario> findByVeterinarioId(Long veterinarioId);
    Page<Prontuario> findByVeterinarioId(Long veterinarioId, Pageable pageable);
    List<Prontuario> findByTutorEmail(String tutorEmail);
    List<Prontuario> findByPetIdOrderByDataAtendimentoDesc(Long petId);
}