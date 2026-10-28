package com.challenger.sprint.javaspg.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

@Service
public class IoTCareService {
    private final RestClient client;
    public IoTCareService(@Value("${IOT_API_URL:http://127.0.0.1:8000}") String baseUrl, @Value("${IOT_INTERNAL_TOKEN:}") String token) {
        var factory=new JdkClientHttpRequestFactory(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build());
        factory.setReadTimeout(Duration.ofSeconds(25));
        var builder=RestClient.builder().baseUrl(baseUrl).requestFactory(factory);
        if (!token.isBlank()) builder.defaultHeader("X-Internal-Token",token);
        client=builder.build();
    }
    public String catalog() {
        try { return client.get().uri("/api/v1/questionario").retrieve().body(String.class); }
        catch(RestClientException error) { throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"O serviço de cuidados está temporariamente indisponível."); }
    }
    public String analyze(Map<String,Object> payload) {
        try { return client.post().uri("/api/v1/analises").body(payload).retrieve().body(String.class); }
        catch(RestClientException error) { throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Não foi possível concluir a análise. Revise os dados ou tente novamente."); }
    }
}
