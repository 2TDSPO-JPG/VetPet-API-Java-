package com.challenger.sprint.javaspg.dto;

import com.challenger.sprint.javaspg.enuns.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ExamesDto {

    private Long id;
    private LocalDateTime dataExame;
    private String petCode;
    private Status status;
    private String nomeDoProfissional;
    private String emailTutores;
    private String motivo;
    private Long petId;
    private Long veterinarioId;
}