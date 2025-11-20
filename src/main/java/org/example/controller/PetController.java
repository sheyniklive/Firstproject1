package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.service.PetServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public ResponseEntity<Void> deleteAllPets(@PathVariable("personId") UUID personId) {
        boolean deleted = petServiceV2.deleteAllPets(personId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
