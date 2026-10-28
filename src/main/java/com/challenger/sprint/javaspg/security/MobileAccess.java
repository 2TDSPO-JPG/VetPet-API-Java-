package com.challenger.sprint.javaspg.security;
import com.challenger.sprint.javaspg.repository.PetRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class MobileAccess {
    private final PetRepository pets;
    private final com.challenger.sprint.javaspg.repository.PerguntasRepository questions;
    private final com.challenger.sprint.javaspg.repository.ExamesRepository exams;
    private final com.challenger.sprint.javaspg.repository.TutorRepository tutors;
    public MobileAccess(PetRepository pets, com.challenger.sprint.javaspg.repository.PerguntasRepository questions, com.challenger.sprint.javaspg.repository.ExamesRepository exams, com.challenger.sprint.javaspg.repository.TutorRepository tutors) { this.pets = pets; this.questions=questions; this.exams=exams; this.tutors=tutors; }
    public MobileSessions.User user() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof MobileSessions.User user)) throw new AccessDeniedException("Entre no aplicativo para continuar.");
        return user;
    }
    public boolean staff() { return !user().role().equals("TUTOR"); }
    public void tutor(Long id) { if (!staff() && !Objects.equals(user().id(), id)) throw new AccessDeniedException("Acesso permitido somente ao titular do cadastro."); }
    public void tutorEmail(String email) { if (!staff() && !tutors.findByEmail(email).map(t -> Objects.equals(t.getId(), user().id())).orElse(false)) throw new AccessDeniedException("Acesso permitido somente ao titular do cadastro."); }
    public void pet(Long id) {
        if (staff()) return;
        boolean owns = pets.findById(id).map(p -> p.getTutores().stream().anyMatch(t -> Objects.equals(t.getId(), user().id()))).orElse(false);
        if (!owns) throw new AccessDeniedException("Pet não disponível para esta conta.");
    }
    public void question(Long id) { questions.findById(id).ifPresent(q -> pet(q.getIdPet())); }
    public void exam(Long id) { exams.findById(id).ifPresent(e -> { if (e.getPet() != null) pet(e.getPet().getId()); }); }
}
