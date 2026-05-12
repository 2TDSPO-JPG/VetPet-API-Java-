package com.challenger.sprint.javaspg.dto.mapper;

import com.challenger.sprint.javaspg.dto.EnderecoDto;
import com.challenger.sprint.javaspg.entity.Endereco;

import java.util.Optional;

public class EnderecoMapper {

    public static EnderecoDto toDto(Optional<Endereco> endereco){
        return EnderecoDto.builder().logradouro(endereco.get().getLogradouro()).numero(endereco.get().getNumero()).complemento(endereco.get().getComplemento()).bairro(endereco.get().getBairro()).cidade(endereco.get().getCidade()).estado(endereco.get().getEstado()).build();
    }

}
