package com.challenger.sprint.javaspg.dto.mapper;

import com.challenger.sprint.javaspg.dto.ProntuarioDto;
import com.challenger.sprint.javaspg.entity.Prontuario;

public class ProntuarioMapper {

    public static ProntuarioDto toDto(Prontuario prontuario) {
        if (prontuario == null) return null;

        return ProntuarioDto.builder()
                .id(prontuario.getId())
                .exameId(prontuario.getExameId())
                .diagnostico(prontuario.getDiagnostico())
                .prescricao(prontuario.getPrescricao())
                .examesSolicitados(prontuario.getExamesSolicitados())
                .observacoes(prontuario.getObservacoes())
                .proximoRetorno(prontuario.getProximoRetorno())
                .veterinarioId(prontuario.getVeterinarioId())
                .veterinarioNome(prontuario.getVeterinarioNome())
                .petId(prontuario.getPetId())
                .petNome(prontuario.getPetNome())
                .tutorEmail(prontuario.getTutorEmail())
                .dataAtendimento(prontuario.getDataAtendimento())
                .dataAtualizacao(prontuario.getDataAtualizacao())
                .finalizado(prontuario.getFinalizado())
                .build();
    }
}