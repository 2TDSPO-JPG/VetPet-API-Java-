package com.challenger.sprint.javaspg.controller;

import com.challenger.sprint.javaspg.dto.PetDto;
import com.challenger.sprint.javaspg.dto.requeste.AfiliarPet;
import com.challenger.sprint.javaspg.dto.requeste.PetRequestDto;
import com.challenger.sprint.javaspg.dto.requeste.PetRequestUploadDto;
import com.challenger.sprint.javaspg.service.PetService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/cadastrar/pet")
    public PetDto criarPet(@RequestBody PetRequestDto pet){
        return petService.criarPet(pet);
    }

    @GetMapping("/buscar/pet-code-")
    public PetDto buscarPetPorPetCode(@RequestParam String petCode){
        return petService.findPetByPetCode(petCode);
    }

    @PostMapping("/afiliar-pet")
    public String afiliarPet(@RequestBody AfiliarPet dto){
        return petService.afiliarPet(dto);
    }

    @PutMapping("/atualizar-pet")
    public PetDto atualizarPet(@RequestBody PetRequestUploadDto pet){
        return petService.atualizarPet(pet);
    }

    @DeleteMapping("/deletar-pet-")
    public String deletarPet(@RequestParam Long id){return petService.deletarPet(id);}

    @DeleteMapping("/deletar-pet-code-")
    public String deletarPetPorPetCode(@RequestParam String petCode){return petService.deletarPetPorPetCode(petCode);}

}
