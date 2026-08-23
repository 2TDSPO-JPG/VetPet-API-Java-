package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.dto.ExamesDto;
import com.challenger.sprint.javaspg.dto.mapper.ExamesMapper;
import com.challenger.sprint.javaspg.entity.Exames;
import com.challenger.sprint.javaspg.entity.Pet;
import com.challenger.sprint.javaspg.entity.Veterinario;
import com.challenger.sprint.javaspg.enuns.Status;
import com.challenger.sprint.javaspg.exception.execptions.CadastroException;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.repository.ExamesRepository;
import com.challenger.sprint.javaspg.repository.PetRepository;
import com.challenger.sprint.javaspg.repository.VeterinarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamesService {

    private final ExamesRepository examesRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;

    public ExamesService(ExamesRepository examesRepository,
                         PetRepository petRepository,
                         VeterinarioRepository veterinarioRepository) {
        this.examesRepository = examesRepository;
        this.petRepository = petRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    public ExamesDto criarExames(ExamesDto dto) {

        if (dto == null) {
            throw new CadastroException(
                    "Dados do exame não podem ser nulos"
            );
        }
        dto.setStatus(Status.AGENDADO);

        // Buscar Pet
        Pet pet = null;
        if (dto.getPetId() != null) {
            pet = petRepository.findById(dto.getPetId())
                    .orElseThrow(() -> new EntidadeNaoPersistidaException("Pet não encontrado com ID: " + dto.getPetId()));
        }

        Veterinario veterinario = null;
        if (dto.getVeterinarioId() != null) {
            veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
                    .orElseThrow(() -> new EntidadeNaoPersistidaException("Veterinário não encontrado com ID: " + dto.getVeterinarioId()));
        }

        Exames exames = Exames.builder()
                .dataExame(dto.getDataExame())
                .petCode(dto.getPetCode())
                .status(dto.getStatus())
                .nomeDoProfissional(dto.getNomeDoProfissional())
                .emailTutores(dto.getEmailTutores())
                .motivo(dto.getMotivo())
                .pet(pet)
                .veterinario(veterinario)
                .build();

        Exames exameSalvo = examesRepository.save(exames);

        return ExamesMapper.toDto(exameSalvo);
    }

    public List<ExamesDto> buscarExamesPorPetCodeList(String petCode) {
        List<Exames> exames = examesRepository.findExamesByPetCode(petCode);

        if (exames.isEmpty()) {
            throw new EntidadeNaoPersistidaException(
                    "Nenhum exame encontrado para o petCode: " + petCode
            );
        }

        return exames.stream()
                .map(ExamesMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExamesDto atualizarStatusExame(Long id, String status) {
        Exames exame = examesRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Exame não encontrado com ID: " + id));

        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            exame.setStatus(statusEnum);
            Exames exameSalvo = examesRepository.save(exame);
            return ExamesMapper.toDto(exameSalvo);
        } catch (IllegalArgumentException e) {
            throw new CadastroException("Status inválido: " + status + ". Valores permitidos: AGENDADO, REALIZADO, CANCELADO, PENDENTE");
        }
    }

    public Page<ExamesDto> buscarExamesPorPetCode(
            String petCode,
            Pageable pageable
    ) {

        Page<Exames> exames =
                examesRepository.findExamesByPetCode(
                        petCode,
                        pageable
                );

        if (exames.isEmpty()) {
            throw new EntidadeNaoPersistidaException(
                    "Nenhum exame encontrado para o petCode: "
                            + petCode
            );
        }

        return exames.map(ExamesMapper::toDto);
    }

    public Page<ExamesDto> buscarExamesPorDataExame(
            LocalDateTime dataExame,
            Pageable pageable
    ) {

        LocalDateTime inicio = dataExame.withNano(0);

        LocalDateTime fim = inicio.plusSeconds(1);

        Page<Exames> exames = examesRepository.findByDataExameBetween(inicio, fim, pageable);

        if (exames.isEmpty()) {
            throw new EntidadeNaoPersistidaException("Nenhum exame encontrado para a data: " + dataExame);
        }
        return exames.map(ExamesMapper::toDto);
    }

    public ExamesDto buscarExameMaisProximo(String petCode) {

        LocalDateTime agora = LocalDateTime.now();

        Exames exame = examesRepository
                .findFirstByPetCodeAndDataExameAfterOrderByDataExameAsc(
                        petCode,
                        agora
                )
                .orElseThrow(() ->
                        new EntidadeNaoPersistidaException(
                                "Nenhum exame futuro encontrado"
                        ));

        return ExamesMapper.toDto(exame);
    }

    public Page<ExamesDto> buscarExamesPorEmailTutores(
            String emailTutores,
            Pageable pageable
    ) {

        Page<Exames> exames =
                examesRepository.findExamesByEmailTutores(
                        emailTutores,
                        pageable
                );

        if (exames.isEmpty()) {
            throw new EntidadeNaoPersistidaException(
                    "Nenhum exame encontrado para o email do tutor: "
                            + emailTutores
            );
        }

        return exames.map(ExamesMapper::toDto);
    }

    public Page<ExamesDto> buscarExamesPorStatus(
            Status status,
            Pageable pageable
    ) {

        Page<Exames> exames =
                examesRepository.findExamesByStatus(
                        status,
                        pageable
                );

        if (exames.isEmpty()) {
            throw new EntidadeNaoPersistidaException(
                    "Nenhum exame encontrado"
            );
        }

        return exames.map(ExamesMapper::toDto);
    }

    public Page<ExamesDto> buscarExamesPorNomeDoProfissional(
            String nomeDoProfissional,
            Pageable pageable
    ) {

        Page<Exames> exames =
                examesRepository.findExamesByNomeDoProfissional(
                        nomeDoProfissional,
                        pageable
                );

        if (exames.isEmpty()) {
            throw new EntidadeNaoPersistidaException(
                    "Nenhum exame encontrado para o profissional: "
                            + nomeDoProfissional
            );
        }

        return exames.map(ExamesMapper::toDto);
    }

    public List<ExamesDto> buscarExamesPorVeterinarioEData(Long veterinarioId, LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);

        List<Exames> exames = examesRepository.findByVeterinarioIdAndDataExameBetween(
                veterinarioId, inicio, fim
        );

        return exames.stream()
                .map(ExamesMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExamesDto cancelarExame(Long id) {
        Exames exame = examesRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Exame não encontrado"));

        exame.setStatus(Status.CANCELADO);

        Exames exameSalvo = examesRepository.save(exame);
        return ExamesMapper.toDto(exameSalvo);
    }

    public List<ExamesDto> buscarExamesPorVeterinario(Long veterinarioId) {
        List<Exames> exames = examesRepository.findByVeterinarioId(veterinarioId);
        return exames.stream()
                .map(ExamesMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ExamesDto> buscarTodosExames() {
        List<Exames> exames = examesRepository.findAll();
        return exames.stream()
                .map(ExamesMapper::toDto)
                .collect(Collectors.toList());
    }
}