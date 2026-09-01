package com.challenger.sprint.javaspg.controller.pages;

import com.challenger.sprint.javaspg.entity.Pet;
import com.challenger.sprint.javaspg.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class PetsController {

    private final PetService petService;

    @GetMapping("/pets")
    public String pets(Model model) {
        List<Pet> pets = petService.listarPets();
        model.addAttribute("pets", pets);
        return "pet";
    }

    @GetMapping()
    public String firstPage(){
        return "firstpage";
    }


}
