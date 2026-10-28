package com.challenger.sprint.javaspg.controller;
import com.challenger.sprint.javaspg.service.PasswordRecoveryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutores")
public class PasswordRecoveryController {
    private final PasswordRecoveryService service;
    public PasswordRecoveryController(PasswordRecoveryService service) { this.service = service; }
    public record Request(@Email @NotBlank String email) {}
    public record Confirmation(@Email @NotBlank String email, @NotBlank String codigo, @NotBlank String novaSenha) {}
    @PostMapping("/recuperar-senha") @ResponseStatus(HttpStatus.ACCEPTED)
    public void request(@Valid @RequestBody Request data) { service.request(data.email()); }
    @PostMapping("/confirmar-recuperacao") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@Valid @RequestBody Confirmation data) { service.confirm(data.email(), data.codigo(), data.novaSenha()); }
}
