package com.challenger.sprint.javaspg.dto.mapper;

import com.challenger.sprint.javaspg.dto.TutorDto;
import com.challenger.sprint.javaspg.entity.Tutor;

import java.util.List;
import java.util.Optional;

public class TutorMapper {

    public static TutorDto toDtoOpn(Optional<Tutor> tutor){
        return TutorDto.builder().id(tutor.get().getId()).nome(tutor.get().getNome()).fotoUrl(tutor.get().getFotoUrl()).cpf(tutor.get().getCpf()).endereco(EnderecoMapper.toDto(Optional.ofNullable(tutor.get().getEndereco()))).email(tutor.get().getEmail()).telefone(tutor.get().getTelefone()).build();
    }

    public static TutorDto toDto(Tutor tutor){
        return TutorDto.builder().id(tutor.getId()).nome(tutor.getNome()).fotoUrl(tutor.getFotoUrl()).cpf(tutor.getCpf()).endereco(EnderecoMapper.toDto(Optional.ofNullable(tutor.getEndereco()))).email(tutor.getEmail()).telefone(tutor.getTelefone()).build();
    }

    public static List<TutorDto> toDtoList(List<Tutor> tutores){
        return tutores.stream().map(TutorMapper::toDto).toList();
    }

}
