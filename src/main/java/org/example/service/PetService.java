package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.entity.PersonEntity;
import org.example.exception.InvalidOwnershipException;
import org.example.exception.PersonNotFoundException;
import org.example.exception.PetNotFoundException;
import org.example.pet.Pet;
import org.example.pet.PetApiMapper;
import org.example.repository.PersonRepository;
import org.example.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PersonRepository personRepo;
    private final PetRepository petRepo;

    @Transactional
    public List<PetResponseDto> saveOrThrow(List<PetCreateDto> petCreateDtos, UUID ownerId) {
        if (petCreateDtos == null || petCreateDtos.isEmpty()) {
            throw new IllegalArgumentException("Список питомцев пуст");
        }
        PersonEntity owner = personRepo.findByIdOrThrowWithoutMapping(ownerId);
        List<Pet> pets = petCreateDtos.stream()
                .map(PetApiMapper::toDomain)
                .toList();
        List<Pet> savedPets = petRepo.save(pets, owner);
        log.info("Персону с id {} успешно добавлены питомцы: {}", ownerId, savedPets);

        return savedPets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> findByOwnerIdOrThrow(UUID ownerId) {
        if (!personRepo.existsById(ownerId)) {
            log.warn("Персона с id {} не найдено", ownerId);
            throw new PersonNotFoundException(ownerId);
        }
        List<Pet> pets = petRepo.findByOwnerId(ownerId);
        log.info("Из БД успешно загружены питомцы персона с id {}", ownerId);
        return pets.stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteAllOrThrow(UUID ownerId) {
        if (!personRepo.existsById(ownerId)) {
            log.warn("Человек с id {} не найден", ownerId);
            throw new PersonNotFoundException(ownerId);
        }
        Integer deleted = petRepo.deleteAllByOwnerId(ownerId);
        if (deleted == 0) {
            log.info("У персона с id {} нет питомцев для удаления", ownerId);
        } else {
            log.info("Все питомцы персона с id {} удалены", ownerId);
        }
    }

    @Transactional
    public void deleteByOwnerAndIdOrThrow(UUID ownerId, Long id) {
        if (!personRepo.existsById(ownerId)) {
            log.warn("Человека с id {} не найдено", ownerId);
            throw new PersonNotFoundException(ownerId);
        }
        Integer deleted = petRepo.deleteByOwnerAndId(ownerId, id);
        if (deleted == 0) {
            if (!petRepo.existsById(id)) {
                log.warn("Не найдено питомца с id: {}", id);
                throw new PetNotFoundException(id);
            } else {
                throw new InvalidOwnershipException(id, ownerId);
            }
        }
        log.info("У персона с id {} удален питомец с id {}", ownerId, id);
    }
}
