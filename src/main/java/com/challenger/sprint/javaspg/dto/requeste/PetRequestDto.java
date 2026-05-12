package com.challenger.sprint.javaspg.dto.requeste;

import java.time.LocalDate;
import java.util.List;

public record PetRequestDto(
        Long id,
        String name,
        LocalDate dataNascimento,
        String raca,
        List<Long>tutores) {}
