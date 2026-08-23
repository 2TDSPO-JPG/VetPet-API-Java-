package com.challenger.sprint.javaspg.repository;

import com.challenger.sprint.javaspg.entity.Perguntas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerguntasRepository extends JpaRepository<Perguntas, Long> {
    List<Perguntas> findByIdPet(Long idPet);
}
