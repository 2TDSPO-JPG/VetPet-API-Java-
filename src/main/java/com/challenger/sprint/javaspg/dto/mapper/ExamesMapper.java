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
                .motivo(exames.getMotivo())
                .petId(exames.getPet() != null ? exames.getPet().getId() : null)
                .veterinarioId(exames.getVeterinario() != null ? exames.getVeterinario().getId() : null)
                .build();
    }

    public static ExamesDto toDtoOpn(Optional<Exames> exames) {
        if (exames.isEmpty()) return null;
        Exames examesExistente = exames.get();
        return ExamesDto.builder()
                .id(examesExistente.getId())
                .dataExame(examesExistente.getDataExame())
                .petCode(examesExistente.getPetCode())
                .status(examesExistente.getStatus())
                .nomeDoProfissional(examesExistente.getNomeDoProfissional())
                .emailTutores(examesExistente.getEmailTutores())
                .motivo(examesExistente.getMotivo())
                .petId(examesExistente.getPet() != null ? examesExistente.getPet().getId() : null)
                .veterinarioId(examesExistente.getVeterinario() != null ? examesExistente.getVeterinario().getId() : null)
                .build();
    }
}