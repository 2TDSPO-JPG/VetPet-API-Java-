package com.challenger.sprint.javaspg.security;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MobileSessions {
    public record User(Long id, String email, String role) {}
    private record Session(User user, Instant expiresAt) {}
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    public String issue(Long id, String email, String role) {
        sessions.entrySet().removeIf(e -> e.getValue().expiresAt().isBefore(Instant.now()));
        byte[] bytes = new byte[32]; random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessions.put(token, new Session(new User(id, email, role), Instant.now().plusSeconds(8 * 3600)));
        return token;
    }
    public Optional<User> find(String token) {
        Session session = sessions.get(token);
        if (session == null) return Optional.empty();
        if (session.expiresAt().isBefore(Instant.now())) { sessions.remove(token); return Optional.empty(); }
        return Optional.of(session.user());
    }
    public void revoke(String token) { sessions.remove(token); }
    public void revokeUser(Long id) { sessions.entrySet().removeIf(e -> java.util.Objects.equals(e.getValue().user().id(), id) && e.getValue().user().role().equals("TUTOR")); }
}
