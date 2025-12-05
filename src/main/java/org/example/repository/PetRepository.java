package org.example.repository;

import org.example.entity.PetEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface PetRepository extends JpaRepository<PetEntity, Long> {


    List<PetEntity> getPetsByOwnerId(UUID ownerId);

    int deleteAllPetsByOwnerId(UUID ownerId);

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
