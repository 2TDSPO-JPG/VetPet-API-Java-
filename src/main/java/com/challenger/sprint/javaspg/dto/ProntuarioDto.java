package com.challenger.sprint.javaspg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProntuarioDto {
    private Long id;
    private Long exameId;
    private String diagnostico;
    private String prescricao;
    private String examesSolicitados;
    private String observacoes;
    private String proximoRetorno;
    private Long veterinarioId;
    private String veterinarioNome;
    private Long petId;
    private String petNome;
    private String tutorEmail;
    private LocalDateTime dataAtendimento;
    private LocalDateTime dataAtualizacao;
    private Boolean finalizado;
}

