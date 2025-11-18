package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.exception.PersonNotFoundException;
import org.example.pet.Pet;
import org.example.pet.PetApiMapper;
import org.example.repository.PersonRepositoryV2;
import org.example.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetServiceV2 {

    private final PersonRepositoryV2 personRepoV2;
    private final PetRepository petRepo;

    public List<PetResponseDto> savePets(List<PetCreateDto> petCreateDtos, UUID personId) {

        if (!personRepoV2.isExistDbPerson(personId)) {
            log.warn("Персон с id {} не найден", personId);
            throw new PersonNotFoundException(personId);
        }
        List<Pet> pets = petCreateDtos.stream()
                .map(PetApiMapper::toDomain)
                .toList();
        pets.forEach(pet -> pet.setOwnerId(personId));
        List<Pet> savedPets = petRepo.saveAll(pets);

        return savedPets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

    public List<PetResponseDto> getPets(UUID personId) {
        if (!personRepoV2.isExistDbPerson(personId)) {
            log.warn("Персона с id {} не найдено", personId);
            throw new PersonNotFoundException(personId);
        }
        List<Pet> pets = petRepo.getPets(personId);
        return pets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

}
