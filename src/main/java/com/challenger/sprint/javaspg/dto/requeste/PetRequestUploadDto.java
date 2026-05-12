package com.challenger.sprint.javaspg.dto.requeste;

import java.time.LocalDate;

public record PetRequestUploadDto(
        Long id,
        String name,
        LocalDate dataNascimento,
        String raca
        ) {}
