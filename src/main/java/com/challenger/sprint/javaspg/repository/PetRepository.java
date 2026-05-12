package com.challenger.sprint.javaspg.repository;

import com.challenger.sprint.javaspg.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Pet findPetByPetCode(String petCode);

}
