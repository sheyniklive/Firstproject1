package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.PersonEntity;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.person.PersonEntityMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PersonRepository {

    private final HibernateUtil hibernateUtil;

    public Person save(Person person) {

        PersonEntity entity = PersonEntityMapper.toEntity(person);
        PersonEntity merged;
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            merged = session.merge(entity);
            transaction.commit();
            log.info("Персон успешно сохранен, id: {}", merged.getId());
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
        return PersonEntityMapper.toDomain(merged);
    }

    public Person findByIdOrThrow(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id не может быть null");
        }
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            PersonEntity entity = session.get(PersonEntity.class, id);
            if (entity == null) {
                throw new PersonNotFoundException(id);
            }
            return PersonEntityMapper.toDomain(entity);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Person> findAll() {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            var query = session.createQuery("SELECT DISTINCT p FROM PersonEntity p LEFT JOIN FETCH p.pets", PersonEntity.class);
            return query.list().stream()
                    .filter(Objects::nonNull)
                    .map(PersonEntityMapper::toDomain)
                    .toList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteByIdOrThrow(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Передан недопустимый id");
        }
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            PersonEntity person = session.get(PersonEntity.class, id);
            if (person == null) {
                throw new PersonNotFoundException(id);
            }
            session.remove(person);
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean existsPersonById(UUID id) {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            NativeQuery<Integer> query = session.createNativeQuery("SELECT COUNT(*) from persons where id = :id", Integer.class);
            query.setParameter("id", id);
            Integer count = query.getSingleResult();
            return count > 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}