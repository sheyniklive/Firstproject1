package org.example.repository;

import org.example.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findPetsByOwnerId(UUID ownerId);

    @Modifying
    @NativeQuery("DELETE FROM pets WHERE person_id = :personId")
    int deleteAllPetsByPersonId(@Param("personId") UUID personId);

    @Modifying
    @NativeQuery("DELETE FROM pets WHERE person_Id = :personId AND id = :id")
    int deletePetByPersonIdAndId(@Param("personId") UUID personId, @Param("id") Long id);

    boolean existsByOwnerIdAndId(UUID ownerId, Long id);
}
