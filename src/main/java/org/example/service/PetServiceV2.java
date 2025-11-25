package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.exception.InvalidOwnershipException;
import org.example.exception.PersonNotFoundException;
import org.example.exception.PetNotFoundException;
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
        if (petCreateDtos == null || petCreateDtos.isEmpty()) {
            log.warn("Список питомцев не может быть пуст");
            throw new IllegalArgumentException("Список питомцев пуст");
        }
        if (!personRepoV2.isExistDbPerson(personId)) {
            log.warn("Персон с id {} не найден", personId);
            throw new PersonNotFoundException(personId);
        }
        List<Pet> pets = petCreateDtos.stream()
                .map(PetApiMapper::toDomain)
                .toList();
        pets.forEach(pet -> pet.setOwnerId(personId));
        List<Pet> savedPets = petRepo.saveAll(pets);
        log.info("Персону с id {} успешно добавлены питомцы: {}", personId, savedPets);

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
        log.info("Из БД успешно загружены питомцы персона с id {}", personId);
        return pets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

    public void deleteAllPets(UUID personId) {
        if (!personRepoV2.isExistDbPerson(personId)) {
            log.warn("Человек с id {} не найден", personId);
            throw new PersonNotFoundException(personId);
        }
        boolean deleted = petRepo.deleteAllPets(personId);
        if (deleted) {
            log.info("Все питомцы персона с id {} удалены", personId);
        } else {
            log.warn("Не удалось удалить питомцев");
        }
    }

    public void deletePetById(UUID personId, Long petId) {
        if (!personRepoV2.isExistDbPerson(personId)) {
            log.warn("Человека с id {} не найдено", personId);
            throw new PersonNotFoundException(personId);
        }
        if (!petRepo.isExistDbPet(petId)) {
            log.warn("Не найдено питомца с id: {}", petId);
            throw new PetNotFoundException(petId);
        }
        if (!petRepo.isValidOwnership(personId, petId)) {
            log.warn("Питомец с id '{}' не принадлежит персону с id '{}'", petId, personId);
            throw new InvalidOwnershipException(petId, personId);
        }
        boolean deleted = petRepo.deletePetById(personId, petId);
        if (deleted) {
            log.info("У персона с id {} удален питомец с id {}", personId, petId);
        } else {
            log.warn("Удаление питомца сорвалось");
        }
    }
}
