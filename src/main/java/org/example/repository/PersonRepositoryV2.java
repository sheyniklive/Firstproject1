package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.PersonEntity;
import org.example.entity.PersonsNamesView;
import org.example.exception.PersonNotFoundException;
import org.example.person.Person;
import org.example.person.PersonEntityMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PersonRepositoryV2 {

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

    public Person findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id не может быть null");
        }
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            PersonEntity entity = session.get(PersonEntity.class, id);
            return entity != null ? PersonEntityMapper.toDomain(entity) : null;
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

    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Передан недопустимый id");
        }
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            PersonEntity person = session.get(PersonEntity.class, id);
            if (person == null) {
                throw new PersonNotFoundException(id);
            }
            session.remove(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean isExistDbData() {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            NativeQuery<Integer> query = session.createNativeQuery("SELECT COUNT(*) from persons", Integer.class);
            Integer count = query.getSingleResult();
            return count > 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean isExistDbPerson(UUID id) {
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

    public Map<String, String> showAllNames() {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            NativeQuery<PersonsNamesView> query = session.createNativeQuery("SELECT id, name FROM persons", PersonsNamesView.class);

            return query.list().stream()
                    .collect(Collectors.toMap(
                            PersonsNamesView::getPersonEntityName,
                            personsNamesView -> String.valueOf(personsNamesView.getPersonEntityId())
                    ));
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}