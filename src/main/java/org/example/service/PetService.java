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
import org.example.repository.PersonRepository;
import org.example.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PersonRepository personRepo;
    private final PetRepository petRepo;

    public List<PetResponseDto> savePetsOrThrow(List<PetCreateDto> petCreateDtos, UUID personId) {

        if (!personRepo.existsPersonById(personId)) {
            log.warn("Персон с id {} не найден", personId);
            throw new PersonNotFoundException(personId);
        }
        if (petCreateDtos == null || petCreateDtos.isEmpty()) {
            throw new IllegalArgumentException("Список питомцев пуст");
        }
        List<Pet> pets = petCreateDtos.stream()
                .map(PetApiMapper::toDomain)
                .toList();
        List<Pet> savedPets = petRepo.saveAll(pets, personId);
        log.info("Персону с id {} успешно добавлены питомцы: {}", personId, savedPets);

        return savedPets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

    public List<PetResponseDto> getPetsByPersonIdOrThrow(UUID personId) {
        if (!personRepo.existsPersonById(personId)) {
            log.warn("Персона с id {} не найдено", personId);
            throw new PersonNotFoundException(personId);
        }
        List<Pet> pets = petRepo.getPetsByPersonId(personId);
        log.info("Из БД успешно загружены питомцы персона с id {}", personId);
        return pets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

    public void deleteAllPetsOrThrow(UUID personId) {
        if (!personRepo.existsPersonById(personId)) {
            log.warn("Человек с id {} не найден", personId);
            throw new PersonNotFoundException(personId);
        }
        boolean deleted = petRepo.deleteAllPets(personId);
        if (deleted) {
            log.info("Все питомцы персона с id {} удалены", personId);
        } else {
            log.info("У персона с id {} нет питомцев для удаления", personId);
        }
    }

    public void deletePetByIdOrThrow(UUID personId, Long petId) {
        if (!personRepo.existsPersonById(personId)) {
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
            throw new IllegalStateException(String.format("У персона с id %s не произошло удаления питомца с id %s", personId, petId));
        }
    }
}
