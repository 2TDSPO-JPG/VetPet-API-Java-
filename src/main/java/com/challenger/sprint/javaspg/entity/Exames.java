package com.challenger.sprint.javaspg.entity;

import com.challenger.sprint.javaspg.enuns.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "VET_EXAMES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Exames {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime dataExame;
    @Column
    private String petCode;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private String nomeDoProfissional;
    @Column
    private String emailTutores;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;



}
