package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.dto.ExamesDto;
import com.challenger.sprint.javaspg.dto.mapper.ExamesMapper;
import com.challenger.sprint.javaspg.entity.Exames;
import com.challenger.sprint.javaspg.enuns.Status;
import com.challenger.sprint.javaspg.exception.execptions.CadastroException;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.repository.ExamesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExamesService {

    private final ExamesRepository examesRepository;

    public ExamesService(ExamesRepository examesRepository) {
        this.examesRepository = examesRepository;
    }

    public ExamesDto criarExames(ExamesDto dto) {

        if (dto == null) {
            throw new CadastroException(
                    "Dados do exame não podem ser nulos"
            );
        }
        dto.setStatus(Status.AGENDADO);

        Exames exames = Exames.builder()
                .dataExame(dto.getDataExame())
                .petCode(dto.getPetCode())
                .status(dto.getStatus())
                .nomeDoProfissional(dto.getNomeDoProfissional())
                .emailTutores(dto.getEmailTutores())
                .build();

        Exames exameSalvo = examesRepository.save(exames);

        return ExamesMapper.toDto(exameSalvo);
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

}
