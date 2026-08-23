package com.challenger.sprint.javaspg.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VET_VETERINARIO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String crmv;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String telefone;

    @Column(columnDefinition = "CLOB")
    private String biografia;

    @Column
    private String especialidade;

    @Column
    private String experiencia;

    @Column
    private boolean primeiroLogin = true;

    @Column(columnDefinition = "CLOB")
    private String fotoUrl;

    @Column(unique = true)
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credencial_id")
    private Credencial credencial;
}