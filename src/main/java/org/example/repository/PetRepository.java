package org.example.repository;

import org.example.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findPetsByOwnerId(UUID ownerId);

    @Modifying
    @Query("DELETE FROM PetEntity p WHERE p.owner.id = :ownerId")
    int deleteAllPetsByOwnerId(@Param("ownerId") UUID ownerId);

    @Modifying
    @Query("DELETE FROM PetEntity p WHERE p.owner.id = :ownerId AND p.id = :id")
    int deletePetByOwnerIdAndId(@Param("ownerId") UUID ownerId, @Param("id") Long id);

    boolean existsByOwnerIdAndId(UUID ownerId, Long id);
}
