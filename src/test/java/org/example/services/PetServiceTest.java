package org.example.services;

import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.exception.InvalidOwnershipException;
import org.example.exception.PersonNotFoundException;
import org.example.exception.PetNotFoundException;
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
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

        verify(petRepo).save(captor.capture(), eq(ownerId));
        List<Pet> givedPets = captor.getValue();
        assertThat(givedPets).hasSize(2)
                .extracting(Pet::getType)
                .containsExactly(PetType.CAT, PetType.DOG);
        assertThat(givedPets).allSatisfy(pet -> assertThat(pet.getId()).isNull());
    }

    @Test
    @DisplayName("addPets: на null кидает IllegalArgumentException")
    void addPets_shouldThrow_whenListNull() {

        assertThatThrownBy(() -> petService.addPets(null, UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Список питомцев пуст");
    }

    @Test
    @DisplayName("addPets: на пустой список кидает IllegalArgumentException")
    void addPets_shouldThrow_whenListEmpty() {
        assertThatThrownBy(() -> petService.addPets(List.of(), UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Список питомцев пуст");
    }

    @Test
    @DisplayName("list: сначала проверяет владельца, потом грузит питомцев")
    void list_shouldReturnPets_inCorrectOrder() {
        UUID ownerId = UUID.randomUUID();
        List<Pet> pets = List.of(new Cat(1L, "Барсик", ownerId), new Dog(2L, "Шарик", ownerId));
        when(personRepo.existsById(ownerId)).thenReturn(true);
        when(petRepo.findByOwnerId(ownerId)).thenReturn(pets);

        List<PetResponseDto> result = petService.list(ownerId);

        assertThat(result).hasSize(2)
                .extracting(PetResponseDto::getName)
                .containsExactly("Барсик", "Шарик");

        InOrder inOrder = Mockito.inOrder(personRepo, petRepo);
        inOrder.verify(personRepo).existsById(ownerId);
        inOrder.verify(petRepo).findByOwnerId(ownerId);
    }

    @Test
    @DisplayName("list: если владельца нет — PersonNotFoundException и без запроса питомцев")
    void list_shouldThrowPNF_whenOwnerMissing() {
        UUID ownerId = UUID.randomUUID();
        when(personRepo.existsById(ownerId)).thenReturn(false);

        assertThatThrownBy(() -> petService.list(ownerId))
                .isInstanceOf(PersonNotFoundException.class);

        verifyNoInteractions(petRepo);
    }

    @Test
    @DisplayName("deleteAll: при существующем владельце удаляет всех питомцев")
    void deleteAll_shouldDelete_whenOwnerAndPetsExists() {
        UUID ownerId = UUID.randomUUID();
        when(personRepo.existsById(ownerId)).thenReturn(true);
        when(petRepo.deleteAllByOwnerId(ownerId)).thenReturn(3);

        petService.deleteAll(ownerId);

        verify(petRepo).deleteAllByOwnerId(ownerId);
    }

    @Test
    @DisplayName("deleteAll: если питомцев не было (0 удалено) — не падает")
    void deleteAll_shouldNotThrow_whenNothingDeleted() {
        UUID ownerId = UUID.randomUUID();
        when(personRepo.existsById(ownerId)).thenReturn(true);
        when(petRepo.deleteAllByOwnerId(ownerId)).thenReturn(0);

        petService.deleteAll(ownerId);

        verify(petRepo).deleteAllByOwnerId(ownerId);
    }

    @Test
    @DisplayName("deleteAll: если владельца нет — PersonNotFoundException и без удаления")
    void deleteAll_shouldThrow_whenOwnerMissing() {
        UUID ownerId = UUID.randomUUID();
        when(personRepo.existsById(ownerId)).thenReturn(false);

        assertThatThrownBy(() -> petService.deleteAll(ownerId))
                .isInstanceOf(PersonNotFoundException.class);

        verify(petRepo, never()).deleteAllByOwnerId(any());
    }

    @Test
    @DisplayName("delete: успешное удаление своего питомца")
    void delete_shouldSucceed_whenPetDeleted() {
        UUID ownerId = UUID.randomUUID();
        Long petId = 7L;
        when(personRepo.existsById(ownerId)).thenReturn(true);
        when(petRepo.deleteByOwnerIdAndId(ownerId, petId)).thenReturn(1);

        petService.deleteByOwnerIdAndId(ownerId, petId);

        verify(petRepo).deleteByOwnerIdAndId(ownerId, petId);
    }

    @Test
    @DisplayName("delete: владельца нет — PersonNotFoundException, до удаления не доходит")
    void delete_shouldThrow_whenOwnerMissing() {
        UUID ownerId = UUID.randomUUID();
        Long petId = 7L;
        when(personRepo.existsById(ownerId)).thenReturn(false);

        assertThatThrownBy(() -> petService.deleteByOwnerIdAndId(ownerId, petId))
                .isInstanceOf(PersonNotFoundException.class);

        verify(petRepo, never()).deleteByOwnerIdAndId(any(), anyLong());
    }

    @Test
    @DisplayName("delete: удалено 0 и питомца нет вовсе — PetNotFoundException")
    void delete_shouldThrow_whenPetDoesNotExist() {
        UUID ownerId = UUID.randomUUID();
        Long petId = 7L;
        when(personRepo.existsById(ownerId)).thenReturn(true);
        when(petRepo.deleteByOwnerIdAndId(ownerId, petId)).thenReturn(0);
        when(petRepo.existsById(petId)).thenReturn(false);

        assertThatThrownBy(() -> petService.deleteByOwnerIdAndId(ownerId, petId))
                .isInstanceOf(PetNotFoundException.class);
    }

    @Test
    @DisplayName("delete: удалено 0, но питомец есть (чужой) — InvalidOwnershipException")
    void delete_shouldThrow_whenPetBelongsToAnother() {
        UUID ownerId = UUID.randomUUID();
        Long petId = 7L;
        when(personRepo.existsById(ownerId)).thenReturn(true);
        when(petRepo.deleteByOwnerIdAndId(ownerId, petId)).thenReturn(0);
        when(petRepo.existsById(petId)).thenReturn(true);

        assertThatThrownBy(() -> petService.deleteByOwnerIdAndId(ownerId, petId))
                .isInstanceOf(InvalidOwnershipException.class);
    }
}
