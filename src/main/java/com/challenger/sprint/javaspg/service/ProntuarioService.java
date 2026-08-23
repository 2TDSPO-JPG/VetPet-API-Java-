package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.dto.ProntuarioDto;
import com.challenger.sprint.javaspg.dto.mapper.ProntuarioMapper;
import com.challenger.sprint.javaspg.entity.Exames;
import com.challenger.sprint.javaspg.entity.Prontuario;
import com.challenger.sprint.javaspg.entity.Veterinario;
import com.challenger.sprint.javaspg.enuns.Status;
import com.challenger.sprint.javaspg.exception.execptions.CadastroException;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.repository.ExamesRepository;
import com.challenger.sprint.javaspg.repository.ProntuarioRepository;
import com.challenger.sprint.javaspg.repository.VeterinarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ExamesRepository examesRepository;
    private final VeterinarioRepository veterinarioRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                             ExamesRepository examesRepository,
                             VeterinarioRepository veterinarioRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.examesRepository = examesRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public ProntuarioDto criarProntuario(ProntuarioDto dto) {
        List<Prontuario> prontuariosExistentes = prontuarioRepository.findByExameId(dto.getExameId());

        if (!prontuariosExistentes.isEmpty()) {
            throw new CadastroException("Já existe um prontuário para este exame ID: " + dto.getExameId());
        }

        // Verificar se o exame existe
        Exames exame = examesRepository.findById(dto.getExameId())
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Exame não encontrado"));

        // Verificar se o veterinário existe
        Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Veterinário não encontrado"));

        // Atualizar status do exame para REALIZADO
        exame.setStatus(Status.REALIZADO);
        examesRepository.save(exame);

        // Criar prontuário
        Prontuario prontuario = Prontuario.builder()
                .exameId(dto.getExameId())
                .diagnostico(dto.getDiagnostico())
                .prescricao(dto.getPrescricao())
                .examesSolicitados(dto.getExamesSolicitados())
                .observacoes(dto.getObservacoes())
                .proximoRetorno(dto.getProximoRetorno())
                .veterinarioId(dto.getVeterinarioId())
                .veterinarioNome(veterinario.getNome())
                .petId(exame.getPet() != null ? exame.getPet().getId() : null)
                .petNome(exame.getPet() != null ? exame.getPet().getName() : null)
                .tutorEmail(exame.getEmailTutores())
                .finalizado(true)
                .build();

        Prontuario salvo = prontuarioRepository.save(prontuario);
        return ProntuarioMapper.toDto(salvo);
    }

    public List<ProntuarioDto> buscarProntuariosPorExame(Long exameId) {
        List<Prontuario> prontuarios = prontuarioRepository.findByExameId(exameId);
        return prontuarios.stream()
                .map(ProntuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProntuarioDto> buscarProntuariosPorPet(Long petId) {
        List<Prontuario> prontuarios = prontuarioRepository.findByPetIdOrderByDataAtendimentoDesc(petId);
        return prontuarios.stream()
                .map(ProntuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProntuarioDto> buscarProntuariosPorVeterinario(Long veterinarioId) {
        List<Prontuario> prontuarios = prontuarioRepository.findByVeterinarioId(veterinarioId);
        return prontuarios.stream()
                .map(ProntuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<ProntuarioDto> buscarProntuariosPorVeterinarioPaginado(Long veterinarioId, Pageable pageable) {
        Page<Prontuario> prontuarios = prontuarioRepository.findByVeterinarioId(veterinarioId, pageable);
        return prontuarios.map(ProntuarioMapper::toDto);
    }

    public ProntuarioDto buscarProntuarioPorId(Long id) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Prontuário não encontrado"));
        return ProntuarioMapper.toDto(prontuario);
    }

    public List<ProntuarioDto> buscarProntuariosPorTutorEmail(String email) {
        List<Prontuario> prontuarios = prontuarioRepository.findByTutorEmail(email);
        return prontuarios.stream()
                .map(ProntuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProntuarioDto atualizarProntuario(Long id, ProntuarioDto dto) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Prontuário não encontrado"));

        prontuario.setDiagnostico(dto.getDiagnostico());
        prontuario.setPrescricao(dto.getPrescricao());
        prontuario.setExamesSolicitados(dto.getExamesSolicitados());
        prontuario.setObservacoes(dto.getObservacoes());
        prontuario.setProximoRetorno(dto.getProximoRetorno());

        Prontuario atualizado = prontuarioRepository.save(prontuario);
        return ProntuarioMapper.toDto(atualizado);
    }
}