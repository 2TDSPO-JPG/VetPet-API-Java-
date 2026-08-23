package com.challenger.sprint.javaspg.controller;

import com.challenger.sprint.javaspg.entity.Perguntas;
import com.challenger.sprint.javaspg.service.PerguntasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perguntas")
public class PerguntasController {

    private final PerguntasService perguntasService;

    public PerguntasController(PerguntasService perguntasService) {
        this.perguntasService = perguntasService;
    }

    @PostMapping
    public ResponseEntity<Perguntas> criarPergunta(@RequestBody Perguntas pergunta) {
        Perguntas novaPergunta = perguntasService.salvar(pergunta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPergunta);
    }

    @GetMapping("/pet/{idPet}")
    public ResponseEntity<List<Perguntas>> buscarPerguntasPorPet(@PathVariable Long idPet) {
        List<Perguntas> perguntas = perguntasService.buscarPorPet(idPet);
        return ResponseEntity.ok(perguntas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Perguntas> atualizar(@PathVariable Long id, @RequestBody Perguntas dados){
        Perguntas atualizada = perguntasService.atualizar(id, dados);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPergunta(@PathVariable Long id) {
        perguntasService.deletarPergunta(id);
        return ResponseEntity.ok("Pergunta deletada com sucesso");
    }
}