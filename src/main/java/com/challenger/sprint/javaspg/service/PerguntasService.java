package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.entity.Perguntas;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.repository.PerguntasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerguntasService {

    private final PerguntasRepository perguntasRepository;

    public PerguntasService(PerguntasRepository perguntasRepository) {
        this.perguntasRepository = perguntasRepository;
    }

    public Perguntas salvar(Perguntas pergunta) {
        return perguntasRepository.save(pergunta);
    }

    public List<Perguntas> buscarPorPet(Long idPet) {
        return perguntasRepository.findByIdPet(idPet);
    }

    public Perguntas atualizar(Long id, Perguntas dados) {
        Perguntas pergunta = perguntasRepository.findById(id).orElseThrow(() ->
                        new RuntimeException("Pergunta não encontrada"));

        pergunta.setPergunta(dados.getPergunta());
        pergunta.setResposta(dados.getResposta());

        return perguntasRepository.save(pergunta);
    }

    public void deletarPergunta(Long id) {
        if (!perguntasRepository.existsById(id)) {
            throw new EntidadeNaoPersistidaException("Pergunta não encontrada");
        }
        perguntasRepository.deleteById(id);
    }
}
