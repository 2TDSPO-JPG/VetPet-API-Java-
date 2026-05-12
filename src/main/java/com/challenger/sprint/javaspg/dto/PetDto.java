package com.challenger.sprint.javaspg.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PetDto {
    private Long id;
    private String name;
    private LocalDate dataNascimento;
    private String petCode;
    private int idade;
    private String raca;
    private int qtdTutores;
    private List<Long> tutores;
}
