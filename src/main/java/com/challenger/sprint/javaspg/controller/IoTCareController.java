package com.challenger.sprint.javaspg.controller;

import com.challenger.sprint.javaspg.security.MobileAccess;
import com.challenger.sprint.javaspg.service.IoTCareService;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
public class IoTCareController {
    private final IoTCareService service;
    private final MobileAccess access;
    public IoTCareController(IoTCareService service,MobileAccess access) { this.service=service; this.access=access; }
    @GetMapping(value="/cuidados/questionario",produces=MediaType.APPLICATION_JSON_VALUE)
    public String catalog() { return service.catalog(); }
    @PostMapping(value="/pets/{id}/cuidados",produces=MediaType.APPLICATION_JSON_VALUE)
    public String analyze(@PathVariable Long id,@RequestBody Map<String,Object> payload) {
        access.pet(id);
        if (!(payload.get("id") instanceof Number number) || number.longValue()!=id) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O formulário deve corresponder ao pet selecionado.");
        return service.analyze(payload);
    }
}
