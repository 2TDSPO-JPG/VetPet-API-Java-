package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.dto.TutorDto;
import com.challenger.sprint.javaspg.dto.mapper.TutorMapper;
import com.challenger.sprint.javaspg.entity.Credencial;
import com.challenger.sprint.javaspg.entity.Tutor;
import com.challenger.sprint.javaspg.exception.execptions.DadoExistenteException;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.exception.execptions.LoginException;
import com.challenger.sprint.javaspg.exception.execptions.SenhaException;
import com.challenger.sprint.javaspg.repository.TutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;

    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public TutorDto login(String email, String senha){

        Optional<Tutor> tutor = tutorRepository.findTutorByEmailAndCredencial_Senha(email, senha);
        var tutorEncontrado = tutor.get();
        if (tutor.isEmpty()) {
            if (tutorRepository.findByEmail(tutorEncontrado.getEmail()).isPresent() || tutorRepository.findByEmail(tutorEncontrado.getCredencial().getSenha()).isPresent()) throw new LoginException("Login incorreto, verifiquei os dados informados");
        }

        var tutor1 = tutor.get();
        tutor1.setUltimoAcesso(LocalDateTime.now());
        tutorRepository.save(tutor1);

        return TutorMapper.toDtoOpn(tutor);
    }

    @Transactional
    public TutorDto cadastro(Tutor tutor){
        if (tutorRepository.findByEmail(tutor.getEmail()).isPresent()) throw new DadoExistenteException("O email já está em uso.");
        if (tutorRepository.findByCpf(tutor.getCpf()).isPresent())throw new DadoExistenteException("O cpf informado ja esta sendo utilizado por outro usuario");
        var credencial = Credencial.builder()
                .senha(tutor.getCredencial().getSenha())
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
        return TutorMapper.toDtoOpn(tutor);
    }

    public TutorDto buscarTutorPorTelefone(String telefone){
        Optional<Tutor> tutor = tutorRepository.findByTelefone(telefone);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        return TutorMapper.toDtoOpn(tutor);
    }

    public String alterarSenha(String email, String novaSenha){
        Optional<Tutor> tutor = tutorRepository.findByEmail(email);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        var tutor1 = tutor.get();
        if (tutor1.getCredencial().getSenha().equals(novaSenha)) throw new SenhaException("A nova senha deve ser diferente da senha atual.");
        tutor1.getCredencial().setSenha(novaSenha);
        tutorRepository.save(tutor1);
        return "Senha alterada com sucesso.";
    }

    public TutorDto buscarTutorPorId(Long id){
        Optional<Tutor> tutor = tutorRepository.findTutorById(id);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        return TutorMapper.toDtoOpn(tutor);
    }

    public List<TutorDto> buscarTodosTutores(){
        List<Tutor> tutores = tutorRepository.findAll();
        return TutorMapper.toDtoList(tutores);
    }

    public String deletarTutor(Long id){
        Optional<Tutor> tutor = tutorRepository.findTutorById(id);
        if(tutor.isEmpty()) throw new EntidadeNaoPersistidaException("Tutor nao foi encontrado");
        tutorRepository.delete(tutor.get());
        return "Tutor deletado com sucesso.";
    }

}
