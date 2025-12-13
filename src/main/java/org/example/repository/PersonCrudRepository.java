package org.example.repository;

import org.example.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonCrudRepository extends JpaRepository<PersonEntity, UUID> {

    @Modifying
    @Query(value = "DELETE FROM persons WHERE id = :id", nativeQuery = true)
    int deletePersonById(@Param("id") UUID id);
}