package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.PetEntity;
import org.example.pet.Pet;
import org.example.pet.PetEntityMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

    public List<Pet> saveAll(List<Pet> pets) {
        if (pets.isEmpty()) {
            throw new IllegalArgumentException("Список питомцев пуст");
        }
        Session session = null;
        Transaction tx = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            List<PetEntity> mergedPets = new ArrayList<>();
            PetEntity mergedPet;
            for (Pet pet : pets) {
                PetEntity petEntity = PetEntityMapper.toEntity(pet);
                mergedPet = session.merge(petEntity);
                mergedPets.add(mergedPet);
            }
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

    public List<Pet> getPets(UUID personId) {
        Session session = null;
        Transaction tx = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            NativeQuery<PetEntity> query = session.createNativeQuery("select * from pets where person_id = :personId");
            query.setParameter("personId", personId);
            List<PetEntity> pets = query.getResultList();
            tx.commit();
            if (pets.isEmpty()) {
                return new ArrayList<>();
            } else {
                return pets.stream()
                        .map(PetEntityMapper::toDomain)
                        .toList();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


}
