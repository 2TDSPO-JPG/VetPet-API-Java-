package com.challenger.sprint.javaspg.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VET_PERGUNTAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perguntas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pergunta;

    private String resposta;

    @Column(nullable = false)
    private Long idPet;
}