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

    public String getHost() {
        return prop.getProperty("db.host", "localhost");
    }

    public int getPort() {
        return Integer.parseInt(prop.getProperty("db.port", "5432"));
    }

    public String getDbName() {
        return prop.getProperty("db.name", "mydb");
    }

    public String getUser() {
        return prop.getProperty("db.user", "postgres");
    }

    public String getPassword() {
        return prop.getProperty("db.password", "postgres");
    }

    public String getDbUrl() {
        return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDbName();
    }
}
