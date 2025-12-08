package org.example.repository;

import org.example.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findPetsByOwnerId(UUID ownerId);

    int deleteAllPetsByOwnerId(UUID ownerId);

    int deletePetByOwnerIdAndId(UUID ownerId, Long id);

    boolean existsByOwnerIdAndId(UUID ownerId, Long id);
}
