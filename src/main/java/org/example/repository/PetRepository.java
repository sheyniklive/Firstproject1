package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.PersonEntity;
import org.example.entity.PetEntity;
import org.example.pet.Pet;
import org.example.pet.PetEntityMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PetRepository {

    private final HibernateUtil hibernateUtil;

    public List<Pet> saveAll(List<Pet> pets, UUID personId) {
        Session session = null;
        Transaction tx;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            PersonEntity personEntity = session.get(PersonEntity.class, personId);
            List<PetEntity> mergedPets = new ArrayList<>();
            for (Pet pet : pets) {
                PetEntity petEntity = PetEntityMapper.toEntity(pet);
                personEntity.addPet(petEntity);
                PetEntity mergedPet = session.merge(petEntity);
                mergedPets.add(mergedPet);
            }
            mergedPets.forEach(petEntity -> Hibernate.initialize(petEntity.getOwner()));
            tx.commit();
            return mergedPets.stream()
                    .map(PetEntityMapper::toDomain)
                    .toList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Pet> getPetsByPersonId(UUID personId) {
        Session session = null;
        Transaction tx;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            NativeQuery<PetEntity> query = session.createNativeQuery(
                    "select pe.* from pets pe where pe.person_id = :personId", PetEntity.class);
            query.setParameter("personId", personId);
            List<PetEntity> pets = query.getResultList();
            pets.forEach(petEntity -> Hibernate.initialize(petEntity.getOwner()));
            tx.commit();
            return pets.stream()
                    .map(PetEntityMapper::toDomain)
                    .toList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean deleteAllPets(UUID personId) {
        Session session = null;
        Transaction tx;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            MutationQuery query = session.createNativeMutationQuery("DELETE from pets WHERE person_id = :personId");
            query.setParameter("personId", personId);
            int deletedLines = query.executeUpdate();
            tx.commit();
            return deletedLines > 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean deletePetById(UUID personId, Long petId) {
        Session session = null;
        Transaction tx;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            MutationQuery query = session.createNativeMutationQuery("DELETE from pets WHERE person_id = :personId AND id = :petId");
            query.setParameter("personId", personId);
            query.setParameter("petId", petId);
            int deletedLines = query.executeUpdate();
            tx.commit();
            return deletedLines > 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean isExistDbPet(Long petId) {
        Session session = null;
        Transaction tx;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            NativeQuery<Integer> query = session.createNativeQuery("SELECT COUNT(*) FROM pets WHERE id = :petId", Integer.class);
            query.setParameter("petId", petId);
            Integer count = query.uniqueResult();
            tx.commit();
            return count > 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean isValidOwnership(UUID personId, Long petId) {
        Session session = null;
        Transaction tx;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            NativeQuery<Integer> query = session.createNativeQuery(
                    "SELECT COUNT(*) FROM pets pe JOIN persons p " +
                            "ON pe.person_id = p.id " +
                            "WHERE pe.id = :petId AND p.id = :personId",
                    Integer.class);
            query.setParameter("petId", petId);
            query.setParameter("personId", personId);
            Integer count = query.uniqueResult();
            tx.commit();
            return count > 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
