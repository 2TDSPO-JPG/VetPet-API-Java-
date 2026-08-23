package com.challenger.sprint.javaspg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "VET_PET")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "O nome não pode ser vazio")
    private String name;

    @Column
    private String petCode;

    @Column
    @NotNull(message = "A data de nascimento não pode ser nula")
    private LocalDate dataNascimento;

    @Column
    private int idade;

    @Column
    private String raca;

    @Column
    private String tipoAnimal;

    @Column
    private int qtdTutores = 1;

    @JsonIgnore
    @ManyToMany(mappedBy = "pets")
    private List<Tutor> tutores = new java.util.ArrayList<>();

    @Column
    private LocalDateTime ultimoAcessoTutor;

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Exames> exames;

    @PrePersist
    public void gerarPetCode() {
        if (this.petCode == null || this.petCode.isEmpty()) {
            this.petCode = "PET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        calcularIdade(); // Chama o cálculo da idade
    }

    @PreUpdate
    public void calcularIdade() {  // <-- REMOVA O PARÂMETRO
        if (this.dataNascimento != null) {
            LocalDate hoje = LocalDate.now();
            int idadeCalculada = hoje.getYear() - this.dataNascimento.getYear();

            // Verifica se já fez aniversário este ano
            if (this.dataNascimento.getMonthValue() > hoje.getMonthValue() ||
                    (this.dataNascimento.getMonthValue() == hoje.getMonthValue() &&
                            this.dataNascimento.getDayOfMonth() > hoje.getDayOfMonth())) {
                idadeCalculada--;
            }

            this.idade = idadeCalculada;
        }
    }
}