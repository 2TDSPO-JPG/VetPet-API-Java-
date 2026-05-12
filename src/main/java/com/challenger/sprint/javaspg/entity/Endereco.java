package com.challenger.sprint.javaspg.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VET_ENDERECO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    private String cidade;

    private String estado;

}
