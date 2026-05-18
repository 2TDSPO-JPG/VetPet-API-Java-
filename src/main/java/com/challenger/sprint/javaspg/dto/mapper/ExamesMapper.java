package com.challenger.sprint.javaspg.dto.mapper;

import com.challenger.sprint.javaspg.dto.ExamesDto;
import com.challenger.sprint.javaspg.entity.Exames;

import java.util.Optional;

public class ExamesMapper {

    public static ExamesDto toDto(Exames exames) {
        if (exames == null) {
            return null;
        }
        return ExamesDto.builder()
                .id(exames.getId())
                .dataExame(exames.getDataExame())
                .petCode(exames.getPetCode())
                .status(exames.getStatus())
                .nomeDoProfissional(exames.getNomeDoProfissional())
                .emailTutores(exames.getEmailTutores())
                .build();
    }

    public static ExamesDto toDtoOpn(Optional<Exames> exames){
        if (exames.isEmpty()) return null;
        Exames examesExistente = exames.get();
        return ExamesDto.builder()
                .id(examesExistente.getId())
                .dataExame(examesExistente.getDataExame())
                .petCode(examesExistente.getPetCode())
                .status(examesExistente.getStatus())
                .nomeDoProfissional(examesExistente.getNomeDoProfissional())
                .emailTutores(examesExistente.getEmailTutores())
                .build();
    }

}
