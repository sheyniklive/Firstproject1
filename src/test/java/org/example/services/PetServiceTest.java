package org.example.services;

import org.example.pet.Pet;
import org.example.repository.PersonRepository;
import org.example.repository.PetRepository;
import org.example.service.PetService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PersonRepository  personRepo;

    @Mock
    private PetRepository petRepo;

    @InjectMocks
    private PetService petService;

    @Captor
    ArgumentCaptor<List<Pet>> captor;

}
