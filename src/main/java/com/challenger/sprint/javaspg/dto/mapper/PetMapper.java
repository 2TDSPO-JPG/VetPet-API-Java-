package com.challenger.sprint.javaspg.dto.mapper;

import com.challenger.sprint.javaspg.dto.PetDto;
import com.challenger.sprint.javaspg.entity.Pet;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;

import java.util.Optional;

public class PetMapper {

    public static PetDto toDto(Pet pet){
        return PetDto.builder().name(pet.getName())
                .dataNascimento(pet.getDataNascimento())
                .idade(pet.getIdade())
                .petCode(pet.getPetCode())
                .raca(pet.getRaca())
                .qtdTutores(pet.getQtdTutores())
                .tutores(pet.getTutores().stream().map(tutor -> tutor.getId()).toList())
                .build();
    }

    public static PetDto toDtoOpn(Optional<Pet> pet){
        if (pet.isEmpty()) new EntidadeNaoPersistidaException("Pet não encontrado");
        Pet petExistente = pet.get();
        return PetDto.builder().name(petExistente.getName())
                .dataNascimento(petExistente.getDataNascimento())
                .idade(petExistente.getIdade())
                .raca(petExistente.getRaca())
                .qtdTutores(petExistente.getQtdTutores())
                .build();
    }

}
