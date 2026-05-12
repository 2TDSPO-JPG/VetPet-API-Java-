package com.challenger.sprint.javaspg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "VET_CREDENCIAL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotBlank(message = "A senha é obrigatória")
    private String senha;
    @OneToOne
    @JoinColumn(name = "vet_tutor_id")
    @ToString.Exclude
    @JsonIgnore
    private Tutor tutor;

}
