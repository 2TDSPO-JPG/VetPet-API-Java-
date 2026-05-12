package com.challenger.sprint.javaspg.controller;

import com.challenger.sprint.javaspg.dto.function.LoginDto;
import com.challenger.sprint.javaspg.dto.TutorDto;
import com.challenger.sprint.javaspg.entity.Tutor;
import com.challenger.sprint.javaspg.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    private TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @PostMapping
    public ResponseEntity<TutorDto> cadastro(@Valid @RequestBody Tutor tutor) {
            TutorDto tutor1 = tutorService.cadastro(tutor);
            return ResponseEntity.status(HttpStatus.CREATED).body(tutor1);
    }

    @PostMapping("/login")
    public ResponseEntity<TutorDto> login(@RequestBody LoginDto loginDto){
        TutorDto tutor = tutorService.login(loginDto.getEmail(), loginDto.getSenha());
        return ResponseEntity.status(HttpStatus.CREATED).body(tutor);
    }

    @GetMapping("/buscar-por-cpf")
    public ResponseEntity<TutorDto> buscarTutorPorCpf(@RequestParam String cpf) {
        TutorDto tutor = tutorService.buscarTutorPorCpf(cpf);
        return ResponseEntity.ok(tutor);
    }

    @GetMapping("/buscar-por-telefone")
    public ResponseEntity<TutorDto> buscarTutorPorTelefone(@RequestParam String telefone) {
        TutorDto tutor = tutorService.buscarTutorPorTelefone(telefone);
        return ResponseEntity.ok(tutor);
    }

    @PutMapping("/alterar-senha/{email}-{novaSenha}")
    public ResponseEntity<String> alterarSenha(@PathVariable("email") String email, @PathVariable("novaSenha") String novaSenha) {
        String resposta = tutorService.alterarSenha(email, novaSenha);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/buscar-por-id")
    public ResponseEntity<TutorDto> buscarTutorPorId(@RequestParam Long id) {
        TutorDto tutor = tutorService.buscarTutorPorId(id);
        return ResponseEntity.ok(tutor);
    }

    @GetMapping
    public ResponseEntity<List<TutorDto>> buscarTodosTutores() {
        List<TutorDto> tutores = tutorService.buscarTodosTutores();
        return ResponseEntity.ok(tutores);
    }

    @DeleteMapping
    public ResponseEntity<String> deletarTutor(@RequestParam Long id) {
        String resposta = tutorService.deletarTutor(id);
        return ResponseEntity.ok(resposta);
    }

}
