package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.repository.TutorRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordRecoveryService {
    private record Code(String hash, Instant expiresAt, int attempts) {}
    private final ConcurrentHashMap<String, Code> codes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Instant> requested = new ConcurrentHashMap<>();
    private final TutorRepository tutors;
    private final ObjectProvider<JavaMailSender> mail;
    private final PasswordEncoder passwords;
    private final com.challenger.sprint.javaspg.security.MobileSessions sessions;
    @Value("${APP_MAIL_FROM:}") private String from;
    public PasswordRecoveryService(TutorRepository tutors, ObjectProvider<JavaMailSender> mail, PasswordEncoder passwords, com.challenger.sprint.javaspg.security.MobileSessions sessions) { this.tutors = tutors; this.mail = mail; this.passwords = passwords; this.sessions=sessions; }
    private static String hash(String code) {
        try { return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(code.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
    public synchronized void request(String address) {
        JavaMailSender sender = mail.getIfAvailable();
        if (sender == null || from.isBlank()) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "O envio de recuperação ainda não foi configurado no servidor.");
        String email = address.trim().toLowerCase(java.util.Locale.ROOT);
        codes.entrySet().removeIf(e -> e.getValue().expiresAt().isBefore(Instant.now()));
        requested.entrySet().removeIf(e -> e.getValue().isBefore(Instant.now().minusSeconds(900)));
        if (requested.getOrDefault(email, Instant.EPOCH).isAfter(Instant.now().minusSeconds(60))) throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Aguarde um minuto antes de solicitar outro código.");
        requested.put(email, Instant.now());
        if (tutors.findByEmail(email).isEmpty()) return;
        String value = String.format("%06d", new SecureRandom().nextInt(1000000));
        codes.put(email, new Code(hash(value), Instant.now().plusSeconds(900), 0));
        SimpleMailMessage message = new SimpleMailMessage(); message.setFrom(from); message.setTo(email); message.setSubject("CLYVO VET — recuperação de acesso");
        message.setText("Seu código de recuperação é " + value + ". Ele expira em 15 minutos. Se você não solicitou a recuperação, ignore esta mensagem.");
        try { sender.send(message); }
        catch (Exception e) { codes.remove(email); throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível enviar o código. Tente novamente mais tarde."); }
    }
    public synchronized void confirm(String address, String value, String newPassword) {
        if (newPassword == null || newPassword.length() < 8 || newPassword.length() > 72) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter entre 8 e 72 caracteres.");
        String email = address.trim().toLowerCase(java.util.Locale.ROOT);
        Code code = codes.get(email);
        if (code == null || code.expiresAt().isBefore(Instant.now()) || code.attempts() >= 5) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido ou expirado.");
        if (!MessageDigest.isEqual(code.hash().getBytes(StandardCharsets.UTF_8), hash(value).getBytes(StandardCharsets.UTF_8))) {
            codes.put(email, new Code(code.hash(), code.expiresAt(), code.attempts() + 1)); throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido ou expirado.");
        }
        var tutor = tutors.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido ou expirado."));
        tutor.getCredencial().setSenha(passwords.encode(newPassword)); tutors.save(tutor); codes.remove(email); sessions.revokeUser(tutor.getId());
    }
}
