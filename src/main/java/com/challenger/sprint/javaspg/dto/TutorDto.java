package com.challenger.sprint.javaspg.dto;

import com.challenger.sprint.javaspg.entity.Endereco;
import com.challenger.sprint.javaspg.entity.Pet;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TutorDto {
    private Long id;
    private String nome;
    private String fotoUrl;
    private String cpf;
    private EnderecoDto endereco;
    private String email;
    private String telefone;

    public TutorDto(Long id, @NotBlank(message = "O nome do tutor é obrigatório") String nome, String fotoUrl, @NotBlank(message = "O CPF do tutor é obrigatório") String cpf, Endereco endereco, @NotBlank(message = "O email do tutor é obrigatório") String email, String telefone, List<Pet> pets, LocalDateTime ultimoAcesso) {
    }
}
