package com.challenger.sprint.javaspg.repository;

import com.challenger.sprint.javaspg.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findTutorById(Long id);
    Optional<Tutor> findTutorByEmailAndCredencial_Senha(String email, String credencialSenha);
    Optional<Tutor> findByEmail(String email);
    Optional<Tutor> findByCpf(String cpf);
    Optional<Tutor> findByTelefone(String telefone);
}
