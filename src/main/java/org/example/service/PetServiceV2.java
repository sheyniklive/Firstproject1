package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.entity.PersonEntity;
import org.example.entity.PetEntity;
import org.example.person.PersonEntityMapper;
import org.example.pet.PetApiMapper;
import org.example.pet.PetEntityMapper;
import org.example.repository.PersonRepositoryV2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetServiceV2 {

    private final PersonRepositoryV2 repoV2;

    public List<PetResponseDto> savePets(List<PetCreateDto> petCreateDtos, UUID personId) {

        PersonEntity entity = PersonEntityMapper.toEntity(repoV2.findById(personId));
        List<PetEntity> pets = petCreateDtos.stream()
                .map(PetApiMapper::toDomain)
                .map(PetEntityMapper::toEntity)
                .toList();
        pets.forEach(entity::addPet);
        PersonEntity savedEntity = repoV2.save(PersonEntityMapper.toDomain(entity));
        return savedEntity.getPets().stream()
                .map(PetApiMapper::toResponse)
                .toList();
    }

}
