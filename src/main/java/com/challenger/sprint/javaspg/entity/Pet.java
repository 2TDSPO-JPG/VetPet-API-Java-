package com.challenger.sprint.javaspg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private LocalDate dataNascimento;
    @Column
    private int idade;
    @Column
    private String raca;
    @Column
    private int qtdTutores;
    @JsonIgnore
    @ManyToMany(mappedBy = "pets")
    private List<Tutor> tutores = new java.util.ArrayList<>();
    @Column
    private LocalDateTime ultimoAcessoTutor;

    @PrePersist
    public void gerarPetCode(){
        if (this.petCode == null || this.petCode.isEmpty()){
            this.petCode = "PET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    public int calcularIdade(LocalDate dataNascimento){
        if (this.dataNascimento != null){
            int idadeCalculada = LocalDate.now().getYear() - this.dataNascimento.getYear();
            this.setIdade(idadeCalculada);
            return idadeCalculada;
        }
        return 0;
    }

}
