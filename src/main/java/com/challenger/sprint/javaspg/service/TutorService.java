package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.dto.TutorDto;
import com.challenger.sprint.javaspg.dto.mapper.TutorMapper;
import com.challenger.sprint.javaspg.entity.Credencial;
import com.challenger.sprint.javaspg.entity.Tutor;
import com.challenger.sprint.javaspg.exception.execptions.DadoExistenteException;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.exception.execptions.LoginException;
import com.challenger.sprint.javaspg.exception.execptions.SenhaException;
import com.challenger.sprint.javaspg.repository.PetRepository;
import com.challenger.sprint.javaspg.repository.TutorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.challenger.sprint.javaspg.entity.Endereco;

@Service
@AllArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwords;
    private final com.challenger.sprint.javaspg.security.MobileAccess access;

    public TutorDto login(String email, String senha){
        Tutor found = tutorRepository.findByEmail(email.trim().toLowerCase(java.util.Locale.ROOT)).orElseThrow(() -> new LoginException("Credenciais inválidas"));
        String stored = found.getCredencial().getSenha();
        boolean encoded = stored.startsWith("$2");
        if (!(encoded ? passwords.matches(senha, stored) : java.security.MessageDigest.isEqual(stored.getBytes(java.nio.charset.StandardCharsets.UTF_8), senha.getBytes(java.nio.charset.StandardCharsets.UTF_8)))) throw new LoginException("Credenciais inválidas");
        if (!encoded) found.getCredencial().setSenha(passwords.encode(senha));
        found.setUltimoAcesso(LocalDateTime.now()); tutorRepository.save(found);
        return TutorMapper.toDto(found);
    }

    @Transactional
    public TutorDto cadastro(Tutor tutor){
        if (tutorRepository.findByEmail(tutor.getEmail()).isPresent()) throw new DadoExistenteException("O email já está em uso.");
        if (tutorRepository.findByCpf(tutor.getCpf()).isPresent())throw new DadoExistenteException("O cpf informado ja esta sendo utilizado por outro usuario");
        var credencial = Credencial.builder()
                .senha(passwords.encode(tutor.getCredencial().getSenha()))
                .tutor(tutor)
                .build();
        tutor.setCredencial(credencial);
        tutor.setUltimoAcesso(LocalDateTime.now());
        tutorRepository.save(tutor);
        return TutorMapper.toDto(tutor);
    }

    public TutorDto buscarTutorPorCpf(String cpf){
        Optional<Tutor> tutor = tutorRepository.findByCpf(cpf);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        access.tutor(tutor.get().getId());
        return TutorMapper.toDtoOpn(tutor);
    }

    public TutorDto buscarTutorPorTelefone(String telefone){
        Optional<Tutor> tutor = tutorRepository.findByTelefone(telefone);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        access.tutor(tutor.get().getId());
        return TutorMapper.toDtoOpn(tutor);
    }

    public String alterarSenha(String email, String novaSenha){
        access.tutorEmail(email);
        Optional<Tutor> tutor = tutorRepository.findByEmail(email);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        var tutor1 = tutor.get();
        if (tutor1.getCredencial().getSenha().equals(novaSenha)) throw new SenhaException("A nova senha deve ser diferente da senha atual.");
        tutor1.getCredencial().setSenha(passwords.encode(novaSenha));
        tutorRepository.save(tutor1);
        return "Senha alterada com sucesso.";
    }

    public TutorDto buscarTutorPorId(Long id){
        access.tutor(id);
        Optional<Tutor> tutor = tutorRepository.findTutorById(id);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        return TutorMapper.toDtoOpn(tutor);
    }

    public TutorDto buscarTutorPorEmail(String email) {
        access.tutorEmail(email);
        Optional<Tutor> tutor = tutorRepository.findByEmail(email);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor não encontrado");
        return TutorMapper.toDtoOpn(tutor);
    }

    public List<TutorDto> buscarTodosTutores(){
        List<Tutor> tutores = tutorRepository.findAll();
        return TutorMapper.toDtoList(tutores);
    }

    public String deletarTutor(Long id){
        access.tutor(id);
        Optional<Tutor> tutor = tutorRepository.findTutorById(id);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        tutorRepository.delete(tutor.get());
        return "Tutor deletado com sucesso.";
    }

    public TutorDto atualizarTutor(Tutor tutor) {
        access.tutor(tutor.getId());
        Optional<Tutor> tutorExistente = tutorRepository.findById(tutor.getId());
        if (tutorExistente.isEmpty()) {
            throw new EntidadeNaoPersistidaException("Tutor não encontrado");
        }

        Tutor tutorAtual = tutorExistente.get();

        tutorAtual.setNome(tutor.getNome());
        tutorAtual.setEmail(tutor.getEmail());
        tutorAtual.setCpf(tutor.getCpf());
        tutorAtual.setTelefone(tutor.getTelefone());
        tutorAtual.setFotoUrl(tutor.getFotoUrl());

        if (tutor.getEndereco() != null) {

            if (tutorAtual.getEndereco() == null) {

                Endereco novoEndereco = new Endereco();

                novoEndereco.setLogradouro(
                        tutor.getEndereco().getLogradouro()
                );

                novoEndereco.setNumero(
                        tutor.getEndereco().getNumero()
                );

                novoEndereco.setComplemento(
                        tutor.getEndereco().getComplemento()
                );

                novoEndereco.setBairro(
                        tutor.getEndereco().getBairro()
                );

                novoEndereco.setCidade(
                        tutor.getEndereco().getCidade()
                );

                novoEndereco.setEstado(
                        tutor.getEndereco().getEstado()
                );

                tutorAtual.setEndereco(novoEndereco);

            } else {

                tutorAtual.getEndereco().setLogradouro(
                        tutor.getEndereco().getLogradouro()
                );

                tutorAtual.getEndereco().setNumero(
                        tutor.getEndereco().getNumero()
                );

                tutorAtual.getEndereco().setComplemento(
                        tutor.getEndereco().getComplemento()
                );

                tutorAtual.getEndereco().setBairro(
                        tutor.getEndereco().getBairro()
                );

                tutorAtual.getEndereco().setCidade(
                        tutor.getEndereco().getCidade()
                );

                tutorAtual.getEndereco().setEstado(
                        tutor.getEndereco().getEstado()
                );
            }
        }

        if (tutor.getCredencial() != null && tutor.getCredencial().getSenha() != null && !tutor.getCredencial().getSenha().isBlank()) tutorAtual.getCredencial().setSenha(passwords.encode(tutor.getCredencial().getSenha()));
        Tutor tutorSalvo = tutorRepository.save(tutorAtual);
        return TutorMapper.toDto(tutorSalvo);
    }

}
