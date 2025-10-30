package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.PersonEntity;
import org.example.person.Person;
import org.example.person.PersonApiMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class PersonRepositoryV2 {

    public void save(Person person) {
        PersonEntity entity = PersonApiMapper.toEntityFromDomain(person);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            PersonEntity merged = session.merge(entity);
            person.setId(merged.getId());
            transaction.commit();
            log.info("Персон успешно сохранен, id: {}", person.getId());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Ошибка при сохранении персона в БД", e);
            throw new RuntimeException("Сохранение пенсона сорвалось", e);
        } finally {
            session.close();
        }
    }


}
