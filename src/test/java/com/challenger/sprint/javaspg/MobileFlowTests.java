package com.challenger.sprint.javaspg;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT, properties={"spring.profiles.active=test", "spring.datasource.url=jdbc:h2:mem:mobileflow;DB_CLOSE_DELAY=-1"})
class MobileFlowTests {
    @LocalServerPort int port;
    private final HttpClient client=HttpClient.newHttpClient();
    private final JsonMapper json=JsonMapper.builder().build();
    private HttpResponse<String> request(String method, String route, Object body, String token) throws Exception {
        var builder=HttpRequest.newBuilder(URI.create("http://127.0.0.1:"+port+route)).header("Content-Type","application/json").header("Accept","application/json");
        if(token!=null)builder.header("Authorization","Bearer "+token);
        builder.method(method, body==null?HttpRequest.BodyPublishers.noBody():HttpRequest.BodyPublishers.ofString(body instanceof String?(String)body:json.writeValueAsString(body)));
        return client.send(builder.build(),HttpResponse.BodyHandlers.ofString());
    }
    private JsonNode success(HttpResponse<String> response) throws Exception {
        assertTrue(response.statusCode()>=200 && response.statusCode()<300, "HTTP "+response.statusCode()+": "+response.body());
        return json.readTree(response.body());
    }
    @Test void cadastroLoginPetFormularioAgendaEControleDeAcesso() throws Exception {
        String suffix=UUID.randomUUID().toString().replace("-","").substring(0,10);
        String email="tutor-"+suffix+"@example.invalid";
        String password="Testes123!";
        JsonNode tutor=success(request("POST","/tutores","{\"nome\":\"Tutor de teste\",\"cpf\":\""+suffix+"\",\"email\":\""+email+"\",\"credencial\":{\"senha\":\""+password+"\"}}",null));
        JsonNode login=success(request("POST","/tutores/login","{\"email\":\""+email+"\",\"senha\":\""+password+"\"}",null));
        String token=login.get("token").asText(); assertFalse(token.isBlank());
        assertEquals(401,request("GET","/pets/buscar-por-tutor?email="+email,null,null).statusCode());
        JsonNode pet=success(request("POST","/pets/cadastrar/pet","{\"name\":\"Pet de teste\",\"dataNascimento\":\"2022-01-10\",\"raca\":\"SRD\",\"tipoAnimal\":\"Gato\",\"tutores\":["+tutor.get("id").asLong()+"]}",token));
        long petId=pet.get("id").asLong();
        JsonNode list=success(request("GET","/pets/buscar-por-tutor?email="+email,null,token)); assertEquals(1,list.size());
        JsonNode answer=success(request("POST","/perguntas","{\"pergunta\":\"Diabetes\",\"resposta\":\"Sim\",\"idPet\":"+petId+"}",token));
        success(request("PUT","/perguntas/"+answer.get("id").asLong(),"{\"pergunta\":\"Diabetes\",\"resposta\":\"Não\",\"idPet\":"+petId+"}",token));
        assertEquals("Não",success(request("GET","/perguntas/pet/"+petId,null,token)).get(0).get("resposta").asText());
        assertEquals(403,request("GET","/pets/buscar-por-tutor?email=outra-pessoa@example.invalid",null,token).statusCode());
        assertEquals(403,request("GET","/tutores",null,token).statusCode());
        String vetEmail="vet-"+suffix+"@example.invalid";
        JsonNode vet=success(request("POST","/veterinarios","{\"nome\":\"Vet de teste\",\"primeiroLogin\":true,\"crmv\":\"SP-"+suffix+"\",\"email\":\""+vetEmail+"\",\"credencial\":{\"senha\":\""+password+"\"}}",null));
        assertFalse(vet.get("credencial").has("senha"));
        String vetToken=success(request("POST","/veterinarios/login","{\"email\":\""+vetEmail+"\",\"senha\":\""+password+"\"}",null)).get("token").asText();
        String date=LocalDate.now().plusDays(7).toString();
        String appointment="{\"dataExame\":\""+date+"T10:00:00\",\"petCode\":\""+pet.get("petCode").asText()+"\",\"petId\":"+petId+",\"veterinarioId\":"+vet.get("id").asLong()+",\"nomeDoProfissional\":\"Vet de teste\",\"emailTutores\":\""+email+"\",\"motivo\":\"Acompanhamento\"}";
        JsonNode booked=success(request("POST","/exames/cadastrar",appointment,token));
        assertEquals(409,request("POST","/exames/cadastrar",appointment,token).statusCode());
        JsonNode agenda=success(request("GET","/exames/veterinario/"+vet.get("id").asLong()+"/data?data="+date,null,token)); assertEquals(1,agenda.size());
        assertTrue(agenda.get(0).get("emailTutores").isNull());
        assertTrue(agenda.get(0).get("petId").isNull());
        success(request("PUT","/exames/"+booked.get("id").asLong()+"/cancelar",null,token));
        assertEquals("CANCELADO",success(request("GET","/exames/veterinario/"+vet.get("id").asLong(),null,vetToken)).get(0).get("status").asText());
        JsonNode followup=success(request("POST","/exames/cadastrar",appointment,token));
        String record="{\"exameId\":"+followup.get("id").asLong()+",\"diagnostico\":\"Registro de teste\",\"prescricao\":\"Acompanhamento de teste\",\"examesSolicitados\":\"Nenhum\",\"observacoes\":\"Cenário automatizado\",\"proximoRetorno\":\"Revisão\",\"veterinarioId\":"+vet.get("id").asLong()+"}";
        JsonNode savedRecord=success(request("POST","/prontuarios",record,vetToken));
        assertEquals(1,success(request("GET","/prontuarios/exame/"+followup.get("id").asLong(),null,token)).size());
        String otherEmail="outro-"+suffix+"@example.invalid";
        success(request("POST","/tutores","{\"nome\":\"Outro tutor\",\"cpf\":\"outro-"+suffix+"\",\"email\":\""+otherEmail+"\",\"credencial\":{\"senha\":\""+password+"\"}}",null));
        String otherToken=success(request("POST","/tutores/login","{\"email\":\""+otherEmail+"\",\"senha\":\""+password+"\"}",null)).get("token").asText();
        assertEquals(403,request("GET","/tutores/buscar-por-cpf?cpf="+suffix,null,otherToken).statusCode());
        assertEquals(403,request("GET","/prontuarios/"+savedRecord.get("id").asLong(),null,otherToken).statusCode());
        assertEquals(403,request("GET","/prontuarios/veterinario/"+vet.get("id").asLong(),null,otherToken).statusCode());
        assertEquals(403,request("GET","/exames/pet-code/list?petCode="+pet.get("petCode").asText(),null,otherToken).statusCode());
        assertEquals(403,request("DELETE","/pets/deletar-pet-code-?petCode="+pet.get("petCode").asText(),null,otherToken).statusCode());
        assertEquals(503,request("POST","/tutores/recuperar-senha","{\"email\":\""+email+"\"}",null).statusCode());
    }
}
