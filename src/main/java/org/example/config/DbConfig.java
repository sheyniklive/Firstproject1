package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.LoadConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class DbConfig {
    private final Properties prop = new Properties();

    public DbConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new LoadConfigException("Файл application.properties не найден");
            }
            prop.load(input);
        } catch (IOException e) {
            throw new LoadConfigException("Ошибка загрузки конфигурации", e);
        }

    }


    public String getUser() {
        return prop.getProperty("db.user");
    }

    public String getPassword() {
        return prop.getProperty("db.password");
    }

    public String getDbUrl() {
        return prop.getProperty("db.url");
    }
}
