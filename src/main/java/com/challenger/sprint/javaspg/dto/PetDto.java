package com.challenger.sprint.javaspg.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDto {

    private Long id;

    private String name;

    private String petCode;

    private LocalDate dataNascimento;

    private int idade;

    private String raca;

    private String tipoAnimal;

    private int qtdTutores;

    private List<Long> tutores;

    private LocalDateTime ultimoAcessoTutor;
}