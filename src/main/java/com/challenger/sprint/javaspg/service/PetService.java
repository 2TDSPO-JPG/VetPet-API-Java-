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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public PetDto criarPet(PetRequestDto dto) {
        List<Tutor> tutores = tutorRepository.findAllById(dto.tutores());

        if (tutores.isEmpty()) {
            throw new RuntimeException("Tutor não encontrado");
        }

        if (tutores.size() != dto.tutores().size()) {
            throw new RuntimeException("Um ou mais tutores não foram encontrados");
        }

        Pet pet = Pet.builder()
                .name(dto.name())
                .dataNascimento(dto.dataNascimento())
                .raca(dto.raca())
                .tipoAnimal(dto.tipoAnimal())
                .qtdTutores(tutores.size())
                .tutores(tutores)
                .build();

        Pet petSalvo = petRepository.save(pet);

        for (Tutor tutor : tutores) {
            tutor.getPets().add(petSalvo);
        }

        tutorRepository.saveAll(tutores);

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
        petExistente.setTipoAnimal(pet.tipoAnimal());


        petRepository.save(petExistente);
        return PetMapper.toDto(petExistente);
    }

    @Transactional
    public String deletarPet(Long id){

        Pet pet = petRepository.findById(id)
                .orElseThrow(() ->
                        new EntidadeNaoPersistidaException(
                                "Não foi possível encontrar o Pet informado!"
                        ));
        pet.getTutores().forEach(
                tutor -> tutor.getPets().remove(pet)
        );

        pet.getTutores().clear();
        pet.getExames().clear();
        petRepository.save(pet);
        petRepository.delete(pet);

        return "Pet deletado com sucesso";
    }

    @Transactional
    public String deletarPetPorPetCode(String petCode) {

        Pet pet = petRepository.findPetByPetCode(petCode);

        if (pet == null){
            throw new EntidadeNaoPersistidaException(
                    "Não foi possível encontrar o Pet informado!"
            );
        }

        pet.getTutores().forEach(
                tutor -> tutor.getPets().remove(pet)
        );

        pet.getTutores().clear();
        pet.getExames().clear();

        petRepository.save(pet);
        petRepository.delete(pet);
        return "Pet deletado com sucesso";
    }

    public Pet cadastrar(Pet pet) {
        // Salva o pet no banco
        Pet petSalvo = petRepository.save(pet);

        return petSalvo;
    }

    // No PetService.java

    public List<PetDto> buscarPetsPorTutor(String email) {
        // Busca o tutor pelo email
        Tutor tutor = tutorRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoPersistidaException("Tutor não encontrado"));

        // Retorna os pets do tutor convertidos para DTO usando o mapper
        List<Pet> petsDoTutor = tutor.getPets();
        return PetMapper.toDtoList(petsDoTutor);
    }
}
