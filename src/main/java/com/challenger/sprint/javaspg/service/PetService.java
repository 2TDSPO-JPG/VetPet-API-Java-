package com.challenger.sprint.javaspg.service;

import com.challenger.sprint.javaspg.dto.PetDto;
import com.challenger.sprint.javaspg.dto.requeste.AfiliarPet;
import com.challenger.sprint.javaspg.dto.requeste.PetRequestDto;
import com.challenger.sprint.javaspg.dto.mapper.PetMapper;
import com.challenger.sprint.javaspg.dto.requeste.PetRequestUploadDto;
import com.challenger.sprint.javaspg.entity.Pet;
import com.challenger.sprint.javaspg.entity.Tutor;
import com.challenger.sprint.javaspg.exception.execptions.EntidadeNaoPersistidaException;
import com.challenger.sprint.javaspg.repository.PetRepository;
import com.challenger.sprint.javaspg.repository.TutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;

    public PetService(PetRepository petRepository, TutorRepository tutorRepository) {
        this.petRepository = petRepository;
        this.tutorRepository = tutorRepository;
    }

    public PetDto findPetByPetCode(String petCode){
        Pet pet = petRepository.findPetByPetCode(petCode);
        if(pet == null){
            throw new RuntimeException("Pet não encontrado");
        }
        return PetMapper.toDto(pet);
    }

    public PetDto criarPet(PetRequestDto dto){

        List<Tutor> tutores = tutorRepository.findAllById(dto.tutores());
        Tutor tutorExistente = tutores.get(0);

        Pet pet = Pet.builder()
                .name(dto.name())
                .dataNascimento(dto.dataNascimento())
                .raca(dto.raca())
                .qtdTutores(tutores.size() == 0 ? 0 : tutores.size())
                .tutores(tutores)
                .build();
        pet.calcularIdade(pet.getDataNascimento());

        Pet petSalvo = petRepository.save(pet);
        tutorExistente.getPets().add(petSalvo);
        tutorRepository.save(tutorExistente);
        
        return PetMapper.toDto(petSalvo);
    }

    public String afiliarPet(AfiliarPet dto){

        Pet pet = petRepository.findPetByPetCode(dto.petCode());
        if(pet == null){
            throw new RuntimeException("Pet não encontrado");
        }

        Optional<Tutor> tutores = tutorRepository.findTutorById(dto.tutorIds());
        Tutor tutorExistente = tutores.orElseThrow(() -> new RuntimeException("Tutor não encontrado"));

        pet.setQtdTutores(pet.getQtdTutores() + 1);
        pet.getTutores().add(tutorExistente);
        tutorExistente.getPets().add(pet);

        tutorRepository.save(tutorExistente);
        petRepository.save(pet);

        return "Pet " + pet.getName() + " foi afiliado ao tutor " + tutorExistente.getNome();
    }

    public PetDto atualizarPet(PetRequestUploadDto pet){
        Optional<Pet> pet1 = petRepository.findById(pet.id());
        if (pet1.isEmpty()) throw new EntidadeNaoPersistidaException("Não foi possível encontrar o Pet informado!");
        Pet petExistente = pet1.get();

        petExistente.setName(pet.name());
        petExistente.setDataNascimento(pet.dataNascimento());
        petExistente.setRaca(pet.raca());

        petRepository.save(petExistente);
        return PetMapper.toDto(petExistente);
    }

    public String deletarPet(Long id){
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isEmpty()) throw new EntidadeNaoPersistidaException("Não foi possível encontrar o Pet informado!");
        petRepository.delete(pet.get());
        return "Pet deletado com sucesso";
    }

}
