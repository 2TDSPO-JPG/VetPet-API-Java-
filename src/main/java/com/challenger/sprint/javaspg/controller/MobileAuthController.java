package com.challenger.sprint.javaspg.controller;
import com.challenger.sprint.javaspg.dto.function.LoginDto;
import com.challenger.sprint.javaspg.security.MobileSessions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class MobileAuthController {
    private final MobileSessions sessions;
    @Value("${APP_ADMIN_USER:}") private String adminEmail;
    @Value("${APP_ADMIN_PASSWORD:}") private String adminPassword;
    public MobileAuthController(MobileSessions sessions) { this.sessions = sessions; }
    @PostMapping("/admin/login") public Map<String, Object> admin(@RequestBody LoginDto data) {
        if (adminEmail.isBlank() || adminPassword.isBlank() || data.getEmail() == null || data.getSenha() == null || !adminEmail.equalsIgnoreCase(data.getEmail()) || !MessageDigest.isEqual(adminPassword.getBytes(StandardCharsets.UTF_8), data.getSenha().getBytes(StandardCharsets.UTF_8))) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
        return Map.of("token", sessions.issue(0L, adminEmail, "ADMIN"), "usuario", Map.of("email", adminEmail, "tipo", "admin"));
    }
    @PostMapping("/sair") public void logout(@RequestHeader(value="Authorization", defaultValue="") String authorization) { if (authorization.startsWith("Bearer ")) sessions.revoke(authorization.substring(7)); }
}
