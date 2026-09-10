package com.challenger.sprint.javaspg.controller.pages;

import com.challenger.sprint.javaspg.dto.requeste.PetRequestDto;
import com.challenger.sprint.javaspg.dto.requeste.PetRequestUploadDto;
import com.challenger.sprint.javaspg.entity.Credencial;
import com.challenger.sprint.javaspg.entity.Endereco;
import com.challenger.sprint.javaspg.entity.Pet;
import com.challenger.sprint.javaspg.entity.Tutor;
import com.challenger.sprint.javaspg.entity.Veterinario;
import com.challenger.sprint.javaspg.service.PetService;
import com.challenger.sprint.javaspg.service.TutorService;
import com.challenger.sprint.javaspg.service.VeterinarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@AllArgsConstructor
public class OperacoesController {

    private final PetService petService;
    private final TutorService tutorService;
    private final VeterinarioService veterinarioService;

    @GetMapping("/operacoes")
    public String operacoes(Model model) {

        carregarDados(model);

        return "operacoes";
    }

    @PostMapping("/operacoes/pets/criar")
    public String criarPet(
            @RequestParam String name,
            @RequestParam LocalDate dataNascimento,
            @RequestParam(required = false) String raca,
            @RequestParam(required = false) String tipoAnimal,
            @RequestParam Long tutorId,
            RedirectAttributes redirectAttributes
    ) {

        try {

            PetRequestDto dto = new PetRequestDto(
                    null,
                    name,
                    dataNascimento,
                    raca,
                    tipoAnimal,
                    List.of(tutorId)
            );

            petService.criarPet(dto);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Pet cadastrado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/pets/atualizar")
    public String atualizarPet(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam LocalDate dataNascimento,
            @RequestParam(required = false) String raca,
            @RequestParam(required = false) String tipoAnimal,
            RedirectAttributes redirectAttributes
    ) {

        try {

            PetRequestUploadDto dto = new PetRequestUploadDto(
                    id,
                    name,
                    dataNascimento,
                    raca,
                    tipoAnimal
            );

            petService.atualizarPet(dto);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Pet atualizado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/pets/deletar")
    public String deletarPet(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            petService.deletarPet(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Pet deletado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/tutores/criar")
    public String criarTutor(
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String fotoUrl,
            @RequestParam String senha,

            @RequestParam(required = false) String logradouro,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String complemento,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,

            RedirectAttributes redirectAttributes
    ) {

        try {

            Tutor tutor = new Tutor();

            tutor.setNome(nome);
            tutor.setCpf(cpf);
            tutor.setEmail(email);
            tutor.setTelefone(telefone);
            tutor.setFotoUrl(fotoUrl);

            Credencial credencial = new Credencial();
            credencial.setSenha(senha);

            tutor.setCredencial(credencial);

            if (temEndereco(
                    logradouro,
                    numero,
                    complemento,
                    bairro,
                    cidade,
                    estado
            )) {

                tutor.setEndereco(
                        criarEndereco(
                                logradouro,
                                numero,
                                complemento,
                                bairro,
                                cidade,
                                estado
                        )
                );
            }

            tutorService.cadastro(tutor);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Tutor cadastrado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/tutores/atualizar")
    public String atualizarTutor(
            @RequestParam Long id,
            @RequestParam String nome,
            @RequestParam String cpf,
            @RequestParam String email,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String fotoUrl,

            @RequestParam(required = false) String logradouro,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String complemento,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,

            RedirectAttributes redirectAttributes
    ) {

        try {

            Tutor tutor = new Tutor();

            tutor.setId(id);
            tutor.setNome(nome);
            tutor.setCpf(cpf);
            tutor.setEmail(email);
            tutor.setTelefone(telefone);
            tutor.setFotoUrl(fotoUrl);

            if (temEndereco(
                    logradouro,
                    numero,
                    complemento,
                    bairro,
                    cidade,
                    estado
            )) {

                tutor.setEndereco(
                        criarEndereco(
                                logradouro,
                                numero,
                                complemento,
                                bairro,
                                cidade,
                                estado
                        )
                );
            }

            tutorService.atualizarTutor(tutor);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Tutor atualizado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/tutores/deletar")
    public String deletarTutor(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            String resposta = tutorService.deletarTutor(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    resposta
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/veterinarios/criar")
    public String criarVeterinario(
            @RequestParam String nome,
            @RequestParam String crmv,
            @RequestParam String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String especialidade,
            @RequestParam(required = false) String experiencia,
            @RequestParam(required = false) String biografia,
            @RequestParam(required = false) String fotoUrl,
            @RequestParam String senha,
            RedirectAttributes redirectAttributes
    ) {

        try {

            Veterinario veterinario = new Veterinario();

            veterinario.setNome(nome);
            veterinario.setCrmv(crmv);
            veterinario.setEmail(email);
            veterinario.setCpf(cpf);
            veterinario.setTelefone(telefone);
            veterinario.setEspecialidade(especialidade);
            veterinario.setExperiencia(experiencia);
            veterinario.setBiografia(biografia);
            veterinario.setFotoUrl(fotoUrl);

            Credencial credencial = new Credencial();
            credencial.setSenha(senha);

            veterinario.setCredencial(credencial);

            veterinarioService.salvar(veterinario);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Veterinário cadastrado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/veterinarios/atualizar")
    public String atualizarVeterinario(
            @RequestParam Long id,
            @RequestParam String nome,
            @RequestParam String crmv,
            @RequestParam String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String especialidade,
            @RequestParam(required = false) String experiencia,
            @RequestParam(required = false) String biografia,
            @RequestParam(required = false) String fotoUrl,
            @RequestParam(required = false) String senha,
            RedirectAttributes redirectAttributes
    ) {

        try {

            Veterinario veterinario = new Veterinario();

            veterinario.setId(id);
            veterinario.setNome(nome);
            veterinario.setCrmv(crmv);
            veterinario.setEmail(email);
            veterinario.setCpf(cpf);
            veterinario.setTelefone(telefone);
            veterinario.setEspecialidade(especialidade);
            veterinario.setExperiencia(experiencia);
            veterinario.setBiografia(biografia);
            veterinario.setFotoUrl(fotoUrl);

            if (senha != null && !senha.isBlank()) {

                Credencial credencial = new Credencial();
                credencial.setSenha(senha);

                veterinario.setCredencial(credencial);
            }

            veterinarioService.atualizar(veterinario);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Veterinário atualizado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    @PostMapping("/operacoes/veterinarios/deletar")
    public String deletarVeterinario(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            veterinarioService.deletar(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Veterinário deletado com sucesso!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    mensagemErro(e)
            );
        }

        return "redirect:/operacoes";
    }

    private void carregarDados(Model model) {

        model.addAttribute(
                "pets",
                petService.listarPets()
        );

        model.addAttribute(
                "tutores",
                tutorService.buscarTodosTutores()
        );

        model.addAttribute(
                "veterinarios",
                veterinarioService.buscarTodos()
        );
    }

    private Endereco criarEndereco(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado
    ) {

        Endereco endereco = new Endereco();

        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setComplemento(complemento);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setEstado(estado);

        return endereco;
    }

    private boolean temEndereco(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado
    ) {

        return notBlank(logradouro)
                || notBlank(numero)
                || notBlank(complemento)
                || notBlank(bairro)
                || notBlank(cidade)
                || notBlank(estado);
    }

    private boolean notBlank(String valor) {

        return valor != null && !valor.isBlank();
    }

    private String mensagemErro(Exception e) {

        if (e.getMessage() == null || e.getMessage().isBlank()) {

            return "Não foi possível realizar a operação.";
        }

        return e.getMessage();
    }
}