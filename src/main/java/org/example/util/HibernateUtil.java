package org.example.util;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            log.info("Инициализация Hibernate SessionFactory");
            Configuration configuration = new Configuration()
                    .configure("hibernate.cfg.xml");
            log.info("Конфигрурация загружена");
            sessionFactory = configuration.buildSessionFactory();
            log.info("SessionFactory создан");
        } catch (Throwable ex) {
            log.error("Ошибка при создании SessionFactory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory не инициализирован");
        }
        return sessionFactory;
    }

    @PreDestroy
    public void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
            log.info("SessionFactory закрыт");
        }
    }
}
