package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PersonCreateDto;
import org.example.dto.PersonResponseDto;
import org.example.service.PersonServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonServiceV2 serviceV2;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponseDto> getAllPersons() {
        return serviceV2.getAllPersons();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto getPersonById(@PathVariable UUID id) {
        return serviceV2.getPersonById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponseDto createPerson(@RequestBody PersonCreateDto dto) {
        return serviceV2.createPerson(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersonById(@PathVariable UUID id) {
        serviceV2.deletePersonById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponseDto putPerson(@PathVariable UUID id, @RequestBody PersonCreateDto dto) {
        return serviceV2.fullUpdatePerson(id, dto);
    }
}
