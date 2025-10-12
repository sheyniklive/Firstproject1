package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.service.PersonServiceV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonServiceV2 service;

    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getAll() {
        List<PersonResponseDto> persons = service.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getById(@PathVariable UUID id) {
        PersonResponseDto person = service.getPersonById(id);
        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<PersonResponseDto> post(@RequestBody PersonCreateDto dto) {
        PersonResponseDto createdPerson = service.createPerson(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPerson.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        boolean deleted = service.deletePersonById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> put(@PathVariable UUID id, @RequestBody PersonCreateDto dto) {
        PersonResponseDto updatedPerson = service.fullUpdatePerson(id, dto);
        if (updatedPerson == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedPerson);
        }
    }
}
