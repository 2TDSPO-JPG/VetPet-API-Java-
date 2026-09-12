package com.challenger.sprint.javaspg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "VET_PRONTUARIO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exame_id", nullable = false)
    private Long exameId;

    @Column(name = "diagnostico", columnDefinition = "VARCHAR(MAX)")
    private String diagnostico;

    @Column(name = "prescricao", columnDefinition = "VARCHAR(MAX)")
    private String prescricao;

    @Column(name = "exames_solicitados", columnDefinition = "VARCHAR(MAX)")
    private String examesSolicitados;

    @Column(name = "observacoes", columnDefinition = "VARCHAR(MAX)")
    private String observacoes;

    @Column(name = "proximo_retorno", length = 255)
    private String proximoRetorno;

    @Column(name = "veterinario_id", nullable = false)
    private Long veterinarioId;

    @Column(name = "veterinario_nome", nullable = false, length = 255)
    private String veterinarioNome;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "pet_nome", length = 255)
    private String petNome;

    @Column(name = "tutor_email", nullable = false, length = 255)
    private String tutorEmail;

    @CreationTimestamp
    @Column(name = "data_atendimento", updatable = false)
    private LocalDateTime dataAtendimento;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(nullable = false)
    private Boolean finalizado = true;
}