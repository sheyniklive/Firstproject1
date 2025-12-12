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
public interface PetCrudRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findByOwnerId(UUID ownerId);

    @Modifying
    @Query(value = "DELETE FROM pets WHERE person_id = :ownerId", nativeQuery = true)
    int deleteAllByOwnerId(@Param("ownerId") UUID ownerId);

    @Modifying
    @Query(value = "DELETE FROM pets WHERE person_id = :ownerId AND id = :id", nativeQuery = true)
    int deleteByOwnerIdAndId(@Param("ownerId") UUID ownerId, @Param("id") Long id);
}
