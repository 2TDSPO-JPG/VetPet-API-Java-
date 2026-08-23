package com.challenger.sprint.javaspg.controller;

import com.challenger.sprint.javaspg.dto.ProntuarioDto;
import com.challenger.sprint.javaspg.service.ProntuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @PostMapping
    public ResponseEntity<ProntuarioDto> criarProntuario(@RequestBody ProntuarioDto dto) {
        ProntuarioDto prontuario = prontuarioService.criarProntuario(dto);
        return ResponseEntity.ok(prontuario);
    }

    @GetMapping("/exame/{exameId}")
    public ResponseEntity<List<ProntuarioDto>> buscarProntuariosPorExame(@PathVariable Long exameId) {
        List<ProntuarioDto> prontuarios = prontuarioService.buscarProntuariosPorExame(exameId);
        return ResponseEntity.ok(prontuarios);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<ProntuarioDto>> buscarProntuariosPorPet(@PathVariable Long petId) {
        List<ProntuarioDto> prontuarios = prontuarioService.buscarProntuariosPorPet(petId);
        return ResponseEntity.ok(prontuarios);
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<List<ProntuarioDto>> buscarProntuariosPorVeterinario(@PathVariable Long veterinarioId) {
        List<ProntuarioDto> prontuarios = prontuarioService.buscarProntuariosPorVeterinario(veterinarioId);
        return ResponseEntity.ok(prontuarios);
    }

    @GetMapping("/veterinario/{veterinarioId}/paginado")
    public ResponseEntity<Page<ProntuarioDto>> buscarProntuariosPorVeterinarioPaginado(
            @PathVariable Long veterinarioId,
            Pageable pageable) {
        Page<ProntuarioDto> prontuarios = prontuarioService.buscarProntuariosPorVeterinarioPaginado(veterinarioId, pageable);
        return ResponseEntity.ok(prontuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioDto> buscarProntuarioPorId(@PathVariable Long id) {
        ProntuarioDto prontuario = prontuarioService.buscarProntuarioPorId(id);
        return ResponseEntity.ok(prontuario);
    }

    @GetMapping("/tutor")
    public ResponseEntity<List<ProntuarioDto>> buscarProntuariosPorTutorEmail(@RequestParam String email) {
        List<ProntuarioDto> prontuarios = prontuarioService.buscarProntuariosPorTutorEmail(email);
        return ResponseEntity.ok(prontuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProntuarioDto> atualizarProntuario(
            @PathVariable Long id,
            @RequestBody ProntuarioDto dto) {
        ProntuarioDto prontuario = prontuarioService.atualizarProntuario(id, dto);
        return ResponseEntity.ok(prontuario);
    }
}