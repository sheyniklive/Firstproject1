package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.service.PetServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persons/{personId}/pets")
public class PetController {

    private final PetServiceV2 petServiceV2;

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getPets(@PathVariable("personId") UUID personId) {
        List<PetResponseDto> pets = petServiceV2.getPets(personId);
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<List<PetResponseDto>> createPets(@PathVariable("personId") UUID personId,
                                                           @RequestBody List<PetCreateDto> petCreateDtos) {
        List<PetResponseDto> savedPets = petServiceV2.savePets(petCreateDtos, personId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPets);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPets(@PathVariable("personId") UUID personId) {
        boolean deleted = petServiceV2.deleteAllPets(personId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePetById(@PathVariable("personId") UUID personId,
                                        @PathVariable("petId") Long petId) {
        boolean deleted = petServiceV2.deletePetById(personId, petId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
