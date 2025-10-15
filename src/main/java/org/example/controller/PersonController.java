package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.service.PersonServiceV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/persons")
public class PersonController {

    private final PersonServiceV2 service;

    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getAll() {
        List<PersonResponseDto> persons = service.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getById(@PathVariable UUID id) {
        return service.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PersonResponseDto> post(@RequestBody PersonCreateDto dto) {
        PersonResponseDto createdPerson = service.createPerson(dto);

        return ResponseEntity.ok(createdPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        boolean deleted = service.deletePersonById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> put(@PathVariable UUID id, @RequestBody PersonCreateDto dto) {
        return service.fullUpdatePerson(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
