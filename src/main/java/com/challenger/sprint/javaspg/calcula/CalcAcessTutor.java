package com.challenger.sprint.javaspg.calcula;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
public class CalcAcessTutor {

    public static LocalDateTime ultimoAcesso(List<LocalDateTime> acessos){
        LocalDateTime dataAtual = LocalDateTime.now();
        if (acessos.isEmpty()) return null;
        return acessos.stream().min(Comparator.comparing(a -> Duration.between(dataAtual, a).abs().toSeconds())).orElse(null);
    }

}
