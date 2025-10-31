package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.PersonEntity;
import org.example.person.Person;
import org.example.person.PersonApiMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class PersonRepositoryV2 {

    public void save(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Персон не может быть пустым");
        }
        PersonEntity entity = PersonApiMapper.toEntityFromDomain(person);
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            PersonEntity merged = session.merge(entity);
            person.setId(merged.getId());
            transaction.commit();
            log.info("Персон успешно сохранен, id: {}", person.getId());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Ошибка при сохранении персона в БД", e);
            throw new RuntimeException("Сохранение персона сорвалось", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Optional<Person> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id не может быть null");
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            PersonEntity entity = session.get(PersonEntity.class, id);
            return Optional.ofNullable(entity)
                    .map(PersonApiMapper::toDomainFromEntity);
        } catch (Exception e) {
            log.error("Ошибка при загрузке персона с id ({}) из БД", id, e);
            throw new RuntimeException("Загрузка персона по id не удалась", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Person> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

        }
    }
}

}
