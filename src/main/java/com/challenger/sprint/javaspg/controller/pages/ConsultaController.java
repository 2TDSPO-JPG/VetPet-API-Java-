package com.challenger.sprint.javaspg.controller.pages;

import com.challenger.sprint.javaspg.dto.PetDto;
import com.challenger.sprint.javaspg.dto.mapper.PetMapper;
import com.challenger.sprint.javaspg.entity.Pet;
import com.challenger.sprint.javaspg.service.PetService;
import com.challenger.sprint.javaspg.service.TutorService;
import com.challenger.sprint.javaspg.service.VeterinarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class ConsultaController {

    private final PetService petService;
    private final TutorService tutorService;
    private final VeterinarioService veterinarioService;

    @GetMapping("/consulta")
    public String consulta(
            @RequestParam(defaultValue = "tipo") String tipo,
            Model model
    ) {

        model.addAttribute("tipo", tipo);

        if (tipo.equals("pets")) {

            List<Pet> pets = petService.listarPets();
            List<PetDto> petsDto = PetMapper.toDtoList(pets);

            model.addAttribute("pets", petsDto);
        }

        if (tipo.equals("tutores")) {
            model.addAttribute("tutores", tutorService.buscarTodosTutores());
        }

        if (tipo.equals("veterinarios")) {
            model.addAttribute("veterinarios", veterinarioService.buscarTodos());
        }

        if (tipo.equals("todos")) {

            List<Pet> pets = petService.listarPets();
            List<PetDto> petsDto = PetMapper.toDtoList(pets);

            model.addAttribute("pets", petsDto);
            model.addAttribute("tutores", tutorService.buscarTodosTutores());
            model.addAttribute("veterinarios", veterinarioService.buscarTodos());
        }

        return "consulta";
    }
}
