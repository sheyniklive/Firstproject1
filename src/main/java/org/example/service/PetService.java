package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.entity.PersonEntity;
import org.example.entity.PetEntity;
import org.example.exception.InvalidOwnershipException;
import org.example.exception.PersonNotFoundException;
import org.example.exception.PetNotFoundException;
import org.example.pet.Pet;
import org.example.pet.PetApiMapper;
import org.example.pet.PetEntityMapper;
import org.example.repository.PersonRepository;
import org.example.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PersonRepository personRepo;
    private final PetRepository petRepo;

    @Transactional
    public List<PetResponseDto> savePetsOrThrow(List<PetCreateDto> petCreateDtos, UUID personId) {

        if (petCreateDtos == null || petCreateDtos.isEmpty()) {
            throw new IllegalArgumentException("Список питомцев пуст");
        }
        PersonEntity personEntity = personRepo.findById(personId).
                orElseThrow(() -> new PersonNotFoundException(personId));

        List<Pet> pets = petCreateDtos.stream()
                .map(PetApiMapper::toDomain)
                .toList();
        List<PetEntity> savedPets = new ArrayList<>();
        for (Pet pet : pets) {
            PetEntity petEntity = PetEntityMapper.toEntity(pet);
            petEntity.setOwner(personEntity);
            PetEntity savedPet = petRepo.save(petEntity);
            savedPets.add(savedPet);
        }
        log.info("Персону с id {} успешно добавлены питомцы: {}", personId, savedPets);

        return savedPets.stream()
                .map(PetEntityMapper::toDomain)
                .map(PetApiMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getPetsByPersonIdOrThrow(UUID personId) {
        if (!personRepo.existsById(personId)) {
            log.warn("Персона с id {} не найдено", personId);
            throw new PersonNotFoundException(personId);
        }
        List<Pet> pets = petRepo.getPetsByOwnerId(personId).stream()
                .map(PetEntityMapper::toDomain)
                .toList();
        log.info("Из БД успешно загружены питомцы персона с id {}", personId);
        return pets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteAllPetsOrThrow(UUID personId) {
        if (!personRepo.existsById(personId)) {
            log.warn("Человек с id {} не найден", personId);
            throw new PersonNotFoundException(personId);
        }
        Integer deleted = petRepo.deleteAllPetsByOwnerId(personId);
        if (deleted > 0) {
            log.info("Все питомцы персона с id {} удалены", personId);
        } else {
            log.info("У персона с id {} нет питомцев для удаления", personId);
        }
    }

    @Transactional
    public void deletePetByIdOrThrow(UUID personId, Long petId) {
        if (!personRepo.existsById(personId)) {
            log.warn("Человека с id {} не найдено", personId);
            throw new PersonNotFoundException(personId);
        }
        if (!petRepo.existsById(petId)) {
            log.warn("Не найдено питомца с id: {}", petId);
            throw new PetNotFoundException(petId);
        }
        if (!petRepo.existsByOwnerIdAndId(personId, petId)) {
            log.warn("Питомец с id '{}' не принадлежит персону с id '{}'", petId, personId);
            throw new InvalidOwnershipException(petId, personId);
        }
        Integer deleted = petRepo.deletePetByOwnerIdAndId(personId, petId);
        if (deleted > 0) {
            log.info("У персона с id {} удален питомец с id {}", personId, petId);
        } else {
            throw new IllegalStateException(String.format("У персона с id %s не произошло удаления питомца с id %s", personId, petId));
        }
    }
}
