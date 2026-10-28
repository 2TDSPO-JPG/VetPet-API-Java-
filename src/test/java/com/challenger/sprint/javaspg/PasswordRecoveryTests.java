package com.challenger.sprint.javaspg;

import com.challenger.sprint.javaspg.entity.Tutor;
import com.challenger.sprint.javaspg.entity.Credencial;
import com.challenger.sprint.javaspg.repository.TutorRepository;
import com.challenger.sprint.javaspg.service.PasswordRecoveryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.ArgumentCaptor;
import java.util.Optional;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordRecoveryTests {
    @Test void codigoEnviadoPorEmailPodeSerUsadoUmaVez() {
        var repository=mock(TutorRepository.class); var sender=mock(JavaMailSender.class);
        @SuppressWarnings("unchecked") ObjectProvider<JavaMailSender> provider=mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(sender);
        var encoder=new BCryptPasswordEncoder();
        var tutor=Tutor.builder().id(1L).email("teste@example.invalid").credencial(Credencial.builder().senha(encoder.encode("SenhaAntiga123")).build()).build();
        when(repository.findByEmail(tutor.getEmail())).thenReturn(Optional.of(tutor));
        var sessions=new com.challenger.sprint.javaspg.security.MobileSessions();
        String oldSession=sessions.issue(1L,tutor.getEmail(),"TUTOR");
        var service=new PasswordRecoveryService(repository,provider,encoder,sessions); ReflectionTestUtils.setField(service,"from","no-reply@example.invalid");
        service.request(tutor.getEmail());
        var message=ArgumentCaptor.forClass(SimpleMailMessage.class); verify(sender).send(message.capture());
        var match=Pattern.compile("\\b(\\d{6})\\b").matcher(message.getValue().getText()); assertTrue(match.find());
        String code=match.group(1); service.confirm(tutor.getEmail(),code,"NovaSenha123!");
        assertTrue(encoder.matches("NovaSenha123!",tutor.getCredencial().getSenha()));
        assertTrue(sessions.find(oldSession).isEmpty());
        assertThrows(ResponseStatusException.class,()->service.confirm(tutor.getEmail(),code,"OutraSenha123!"));
    }
}
