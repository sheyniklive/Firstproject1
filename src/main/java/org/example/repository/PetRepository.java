package org.example.repository;

import org.example.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface PetRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> getPetsByOwnerId(UUID ownerId);

    int deleteAllPetsByOwnerId(UUID ownerId);

    int deletePetByOwnerIdAndPetId(UUID personId, Long petId);

    boolean existsByOwnerIdAndPetId(UUID ownerId, Long petId);
}
