package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetCreateDto;
import org.example.dto.PetResponseDto;
import org.example.service.PetServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persons/{personId}/pets")
public class PetController {

    private final PetServiceV2 petServiceV2;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PetResponseDto> getPets(@PathVariable("personId") UUID personId) {
        return petServiceV2.getPets(personId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<PetResponseDto> createPets(@PathVariable("personId") UUID personId,
                                           @RequestBody List<PetCreateDto> petCreateDtos) {
        return petServiceV2.savePets(petCreateDtos, personId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllPets(@PathVariable("personId") UUID personId) {
        petServiceV2.deleteAllPets(personId);
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePetById(@PathVariable("personId") UUID personId,
                              @PathVariable("petId") Long petId) {
        petServiceV2.deletePetById(personId, petId);
    }
}
