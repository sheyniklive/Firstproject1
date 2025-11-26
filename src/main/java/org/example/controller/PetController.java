package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getPets(@PathVariable("personId") UUID personId) {
        return ResponseEntity.ok(petService.getPetsByPersonIdOrThrow(personId));
    }

    @PostMapping
    public ResponseEntity<List<PetResponseDto>> createPets(@PathVariable("personId") UUID personId,
                                           @RequestBody List<PetCreateDto> petCreateDtos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.savePetsOrThrow(petCreateDtos, personId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPets(@PathVariable("personId") UUID personId) {
        petService.deleteAllPetsOrThrow(personId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePetById(@PathVariable("personId") UUID personId,
                              @PathVariable("petId") Long petId) {
        petService.deletePetByIdOrThrow(personId, petId);
        return ResponseEntity.noContent().build();
    }
}
