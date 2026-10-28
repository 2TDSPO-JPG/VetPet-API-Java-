package com.challenger.sprint.javaspg.controller;

import com.challenger.sprint.javaspg.dto.function.LoginDto;
import com.challenger.sprint.javaspg.entity.Veterinario;
import com.challenger.sprint.javaspg.service.VeterinarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veterinarios")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;
    private final com.challenger.sprint.javaspg.security.MobileSessions sessions;

    public VeterinarioController(VeterinarioService veterinarioService, com.challenger.sprint.javaspg.security.MobileSessions sessions) {
        this.sessions = sessions;
        this.veterinarioService = veterinarioService;
    }

    @PostMapping
    public ResponseEntity<Veterinario> criar(@RequestBody Veterinario veterinario) {
        Veterinario salvo = veterinarioService.salvar(veterinario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Veterinario>> buscarTodos() {
        List<Veterinario> veterinarios = veterinarioService.buscarTodos();
        return ResponseEntity.ok(veterinarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinario> buscarPorId(@PathVariable Long id) {
        Veterinario veterinario = veterinarioService.buscarPorId(id);
        return ResponseEntity.ok(veterinario);
    }

    @GetMapping("/crmv/{crmv}")
    public ResponseEntity<Veterinario> buscarPorCrmv(@PathVariable String crmv) {
        Veterinario veterinario = veterinarioService.buscarPorCrmv(crmv);
        return ResponseEntity.ok(veterinario);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Veterinario> buscarPorEmail(@PathVariable String email) {
        Veterinario veterinario = veterinarioService.buscarPorEmail(email);
        return ResponseEntity.ok(veterinario);
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') or (hasRole('VET') and principal.id() == #id)")
    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> atualizar(@PathVariable Long id, @RequestBody Veterinario veterinario) {
        veterinario.setId(id);
        Veterinario atualizado = veterinarioService.atualizar(veterinario);
        return ResponseEntity.ok(atualizado);
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        veterinarioService.deletar(id);
        return ResponseEntity.ok("Veterinário deletado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<java.util.Map<String,Object>> login(@RequestBody LoginDto loginDto) {
        Veterinario veterinario = veterinarioService.login(loginDto.getEmail(), loginDto.getSenha());
        return ResponseEntity.ok(java.util.Map.of("token", sessions.issue(veterinario.getId(), veterinario.getEmail(), "VET"), "veterinario", veterinario));
    }

}