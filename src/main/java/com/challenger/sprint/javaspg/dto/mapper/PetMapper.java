package com.challenger.sprint.javaspg.dto.mapper;

import com.challenger.sprint.javaspg.dto.PetDto;
import com.challenger.sprint.javaspg.entity.Pet;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PetMapper {

    public static PetDto toDto(Pet pet) {

        LocalDateTime ultimoAcessoTutor = pet.getTutores()
                .stream()
                .map(tutor -> tutor.getUltimoAcesso())
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .dataNascimento(pet.getDataNascimento())
                .idade(pet.getIdade())
                .petCode(pet.getPetCode())
                .raca(pet.getRaca())
                .tipoAnimal(pet.getTipoAnimal())
                .qtdTutores(pet.getQtdTutores())
                .tutores(
                        pet.getTutores()
                                .stream()
                                .map(tutor -> tutor.getId())
                                .toList()
                )
                .ultimoAcessoTutor(ultimoAcessoTutor)
                .build();
    }

    public static PetDto toDtoOpn(Optional<Pet> pet) {

        if (pet.isEmpty()) {
            throw new EntidadeNaoPersistidaException("Pet não encontrado");
        }

        Pet petExistente = pet.get();

        LocalDateTime ultimoAcessoTutor = petExistente.getTutores()
                .stream()
                .map(tutor -> tutor.getUltimoAcesso())
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return PetDto.builder()
                .id(petExistente.getId())
                .name(petExistente.getName())
                .dataNascimento(petExistente.getDataNascimento())
                .idade(petExistente.getIdade())
                .petCode(petExistente.getPetCode())
                .raca(petExistente.getRaca())
                .tipoAnimal(petExistente.getTipoAnimal())
                .qtdTutores(petExistente.getQtdTutores())
                .ultimoAcessoTutor(ultimoAcessoTutor)
                .build();
    }

    public static List<PetDto> toDtoList(List<Pet> pets) {

        if (pets == null || pets.isEmpty()) {
            return List.of();
        }

        return pets.stream()
                .map(PetMapper::toDto)
                .collect(Collectors.toList());
    }
}