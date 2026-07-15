package org.example.services;

import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.pet.Cat;
import org.example.pet.Dog;
import org.example.pet.Pet;
import org.example.pet.enums.PetType;
import org.example.repository.PersonRepository;
import org.example.repository.PetRepository;
import org.example.service.PetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PersonRepository personRepo;

    @Mock
    private PetRepository petRepo;

    @InjectMocks
    private PetService petService;

    @Captor
    ArgumentCaptor<List<Pet>> captor;

    @Test
    @DisplayName("addPets: маппит и сохраняет питомцев, возвращает ответы")
    void addPets_shouldSaveAndReturnResponses() {

        UUID ownerId = UUID.randomUUID();
        List<PetCreateDto> dtos = List.of(
                new PetCreateDto("Барсик", PetType.CAT),
                new PetCreateDto("Шарик", PetType.DOG));
        List<Pet> savedPets = List.of(
                new Cat(1L, "Барсик", ownerId),
                new Dog(2L, "Шарик", ownerId)
        );
        when(petRepo.save(anyList(), eq(ownerId))).thenReturn(savedPets);

        List<PetResponseDto> result = petService.addPets(dtos, ownerId);

        assertThat(result).hasSize(2)
                .extracting(PetResponseDto::getName)
                .containsExactly("Барсик", "Шарик");



    }
}
