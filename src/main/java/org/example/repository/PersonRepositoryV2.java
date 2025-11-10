package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.PersonEntity;
import org.example.person.Person;
import org.example.person.PersonEntityMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PersonRepositoryV2 {

    private final HibernateUtil hibernateUtil;

    public void save(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Персон не может быть пустым");
        }
        PersonEntity entity = PersonEntityMapper.toEntity(person);
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
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
            session = hibernateUtil.getSessionFactory().openSession();
            PersonEntity entity = session.get(PersonEntity.class, id);
            return Optional.ofNullable(entity)
                    .map(PersonEntityMapper::toDomain);
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

    public boolean deleteById(UUID id) {
        Session session = null;
        boolean deleted = false;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            PersonEntity person = session.get(PersonEntity.class, id);
            if (person != null) {
                session.remove(person);
                transaction.commit();
                deleted = true;
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return deleted;
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

    public Map<String, String> showAllNames() {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Query<Object[]> query = session.createQuery("SELECT p.id, p.name FROM PersonEntity p", Object[].class);
            List<Object[]> resultList = query.list();
            Map<String, String> namesAndId = new HashMap<>();
            for (Object[] row : resultList) {
                String id = String.valueOf(row[0]);
                String name = String.valueOf(row[1]);
                namesAndId.put(id, name);
            }
            return namesAndId;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}