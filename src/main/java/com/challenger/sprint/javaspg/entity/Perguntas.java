package com.challenger.sprint.javaspg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "VET_PERGUNTAS")
public class Perguntas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pergunta;

    private String resposta;

    private Long idPet;



}
