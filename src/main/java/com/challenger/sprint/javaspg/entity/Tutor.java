package com.challenger.sprint.javaspg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "VET_TUTOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotBlank(message = "O nome do tutor é obrigatório")
    private String nome;

    @Column
    private String fotoUrl;

    @Column(unique = true)
    @NotBlank(message = "O CPF do tutor é obrigatório")
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    @ToString.Exclude
    private Endereco endereco;

    @Column(unique = true)
    @NotBlank(message = "O email do tutor é obrigatório")
    private String email;

    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Credencial credencial;

    @Column
    private String telefone;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "tb_tutor_pet",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    private List<Pet> pets;

    private LocalDateTime ultimoAcesso;


}
