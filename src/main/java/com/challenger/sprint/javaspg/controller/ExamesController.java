package com.challenger.sprint.javaspg.controller;

import com.challenger.sprint.javaspg.dto.ExamesDto;
import com.challenger.sprint.javaspg.enuns.Status;
import com.challenger.sprint.javaspg.service.ExamesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exames")
public class ExamesController {

    private final ExamesService examesService;

    public ExamesController(ExamesService examesService1) {
        this.examesService = examesService1;
    }

    @PostMapping("/cadastrar")
    public ExamesDto criarExames(
            @RequestBody ExamesDto dto
    ){
        return examesService.criarExames(dto);
    }

    @GetMapping("/pet-code")
    public Page<ExamesDto> buscarExamesPorPetCode(
            @RequestParam String petCode,
            Pageable pageable
    ){
        return examesService.buscarExamesPorPetCode(
                petCode,
                pageable
        );
    }

    @GetMapping("/pet-code/list")
    public ResponseEntity<List<ExamesDto>> buscarExamesPorPetCodeList(
            @RequestParam String petCode
    ) {
        List<ExamesDto> exames = examesService.buscarExamesPorPetCodeList(petCode);
        return ResponseEntity.ok(exames);
    }

    @GetMapping("/data-exame")
    public Page<ExamesDto> buscarExamesPorDataExame(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dataExame,
            Pageable pageable
    ){
        return examesService.buscarExamesPorDataExame(
                dataExame,
                pageable
        );
    }

    @GetMapping("/mais-proximo")
    public ExamesDto buscarExameMaisProximo(
            @RequestParam String petCode
    ){
        return examesService.buscarExameMaisProximo(
                petCode
        );
    }

    @GetMapping("/email-tutor")
    public Page<ExamesDto> buscarExamesPorEmailTutores(
            @RequestParam String emailTutores,
            Pageable pageable
    ){
        return examesService.buscarExamesPorEmailTutores(
                emailTutores,
                pageable
        );
    }

    @GetMapping("/status")
    public Page<ExamesDto> buscarExamesPorStatus(
            @RequestParam Status status,
            Pageable pageable
    ){
        return examesService.buscarExamesPorStatus(status, pageable);
    }

    @GetMapping("/profissional")
    public Page<ExamesDto> buscarExamesPorNomeDoProfissional(
            @RequestParam String nomeDoProfissional,
            Pageable pageable
    ){
        return examesService.buscarExamesPorNomeDoProfissional(
                nomeDoProfissional,
                pageable
        );
    }

    @GetMapping("/veterinario/{veterinarioId}/data")
    public ResponseEntity<List<ExamesDto>> buscarExamesPorVeterinarioEData(
            @PathVariable Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<ExamesDto> exames = examesService.buscarExamesPorVeterinarioEData(veterinarioId, data);
        return ResponseEntity.ok(exames);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ExamesDto> cancelarExame(@PathVariable Long id) {
        ExamesDto exameCancelado = examesService.cancelarExame(id);
        return ResponseEntity.ok(exameCancelado);
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<List<ExamesDto>> buscarExamesPorVeterinario(@PathVariable Long veterinarioId) {
        List<ExamesDto> exames = examesService.buscarExamesPorVeterinario(veterinarioId);
        return ResponseEntity.ok(exames);
    }

    @GetMapping
    public ResponseEntity<List<ExamesDto>> buscarTodosExames() {
        List<ExamesDto> exames = examesService.buscarTodosExames();
        return ResponseEntity.ok(exames);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamesDto> atualizarStatusExame(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        String status = request.get("status");
        ExamesDto exameAtualizado = examesService.atualizarStatusExame(id, status);
        return ResponseEntity.ok(exameAtualizado);
    }

}
