package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.entity.Credencial;
import com.challenger.sprint.javaspg.entity.Endereco;
import com.challenger.sprint.javaspg.entity.Veterinario;
import com.challenger.sprint.javaspg.exception.execptions.DadoExistenteException;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.exception.execptions.LoginException;
import com.challenger.sprint.javaspg.exception.execptions.SenhaException;
import com.challenger.sprint.javaspg.repository.VeterinarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioService(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    public Veterinario login(String email, String senha) {
        Optional<Veterinario> veterinario = veterinarioRepository.findByEmail(email);
        if (veterinario.isEmpty()) {
            throw new LoginException("Email não encontrado");
        }

        Veterinario vet = veterinario.get();
        if (vet.getCredencial() == null || !vet.getCredencial().getSenha().equals(senha)) {
            throw new LoginException("Senha incorreta");
        }

        return vet;
    }

    @Transactional
    public Veterinario salvar(Veterinario veterinario) {
        if (veterinarioRepository.findByEmail(veterinario.getEmail()).isPresent()) {
            throw new DadoExistenteException("O email já está em uso.");
        }

        // Criar credencial
        if (veterinario.getCredencial() != null) {
            Credencial credencial = new Credencial();
            credencial.setSenha(veterinario.getCredencial().getSenha());
            credencial.setVeterinario(veterinario);
            veterinario.setCredencial(credencial);
        }

        return veterinarioRepository.save(veterinario);
    }

    public List<Veterinario> buscarTodos() {
        return veterinarioRepository.findAll();
    }

    public Veterinario buscarPorId(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Veterinário não encontrado"));
    }

    public Veterinario buscarPorCrmv(String crmv) {
        return veterinarioRepository.findByCrmv(crmv)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Veterinário não encontrado com CRMV: " + crmv));
    }

    public Veterinario buscarPorEmail(String email) {
        return veterinarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Veterinário não encontrado com email: " + email));
    }

    @Transactional
    public Veterinario atualizar(Veterinario veterinario) {
        Optional<Veterinario> veterinarioExistente = veterinarioRepository.findById(veterinario.getId());
        if (veterinarioExistente.isEmpty()) {
            throw new EntidadeNaoPersistidaException("Veterinário não encontrado");
        }

        Veterinario vetAtual = veterinarioExistente.get();

        vetAtual.setNome(veterinario.getNome());
        vetAtual.setEmail(veterinario.getEmail());
        vetAtual.setCpf(veterinario.getCpf());
        vetAtual.setTelefone(veterinario.getTelefone());
        vetAtual.setFotoUrl(veterinario.getFotoUrl());
        vetAtual.setCrmv(veterinario.getCrmv());
        vetAtual.setEspecialidade(veterinario.getEspecialidade());
        vetAtual.setExperiencia(veterinario.getExperiencia());
        vetAtual.setBiografia(veterinario.getBiografia());
        vetAtual.setPrimeiroLogin(veterinario.isPrimeiroLogin());

        if (veterinario.getEndereco() != null) {
            if (vetAtual.getEndereco() != null) {
                vetAtual.getEndereco().setLogradouro(veterinario.getEndereco().getLogradouro());
                vetAtual.getEndereco().setNumero(veterinario.getEndereco().getNumero());
                vetAtual.getEndereco().setComplemento(veterinario.getEndereco().getComplemento());
                vetAtual.getEndereco().setBairro(veterinario.getEndereco().getBairro());
                vetAtual.getEndereco().setCidade(veterinario.getEndereco().getCidade());
                vetAtual.getEndereco().setEstado(veterinario.getEndereco().getEstado());
            } else {
                Endereco novoEndereco = new Endereco();
                novoEndereco.setLogradouro(veterinario.getEndereco().getLogradouro());
                novoEndereco.setNumero(veterinario.getEndereco().getNumero());
                novoEndereco.setComplemento(veterinario.getEndereco().getComplemento());
                novoEndereco.setBairro(veterinario.getEndereco().getBairro());
                novoEndereco.setCidade(veterinario.getEndereco().getCidade());
                novoEndereco.setEstado(veterinario.getEndereco().getEstado());
                vetAtual.setEndereco(novoEndereco);
            }
        }

        if (veterinario.getCredencial() != null && veterinario.getCredencial().getSenha() != null) {
            if (vetAtual.getCredencial() != null) {
                vetAtual.getCredencial().setSenha(veterinario.getCredencial().getSenha());
            } else {
                Credencial novaCredencial = new Credencial();
                novaCredencial.setSenha(veterinario.getCredencial().getSenha());
                novaCredencial.setVeterinario(vetAtual);
                vetAtual.setCredencial(novaCredencial);
            }
        }

        Veterinario veterinarioSalvo = veterinarioRepository.save(vetAtual);
        return veterinarioSalvo;
    }

    @Transactional
    public void deletar(Long id) {
        Veterinario existente = buscarPorId(id);
        veterinarioRepository.delete(existente);
    }
}