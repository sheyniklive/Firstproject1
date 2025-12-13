package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.entity.PersonEntity;
import org.example.entity.PetEntity;
import org.example.exception.PersonNotFoundException;
import org.example.pet.Pet;
import org.example.pet.PetEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PetRepository {
    private final PersonCrudRepository personCrudRepository;
    private final PetCrudRepository petCrudRepository;

    public List<Pet> save(List<Pet> pets, UUID ownerId) {
        PersonEntity owner = personCrudRepository.findById(ownerId)
                .orElseThrow(() -> new PersonNotFoundException(ownerId));
        List<PetEntity> petEntities = pets.stream()
                .map(pet -> {
                    PetEntity petEntity = PetEntityMapper.toEntity(pet);
                    petEntity.setOwner(owner);
                    return petEntity;
                })
                .toList();

        return petCrudRepository.saveAll(petEntities).stream()
                .map(PetEntityMapper::toDomain)
                .toList();
    }

    public List<Pet> findByOwnerId(UUID ownerId) {
        List<PetEntity> petEntities = petCrudRepository.findByOwnerId(ownerId);
        return petEntities.stream()
                .map(PetEntityMapper::toDomain)
                .toList();
    }

    public Integer deleteAllByOwnerId(UUID ownerId) {
        return petCrudRepository.deleteAllByOwnerId(ownerId);
    }

    public Integer deleteByOwnerIdAndId(UUID ownerId, Long id) {
        return petCrudRepository.deleteByOwnerIdAndId(ownerId, id);
    }

    public boolean existsById(Long id) {
        return petCrudRepository.existsById(id);
    }
}