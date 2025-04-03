package org.grupp2.sdpproject.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_FILE_NAME = "app.properties";
    private final Properties props;

    public ConfigManager() {
        this.props = new Properties();
    }

    public boolean configFileExists() {
        return Files.exists(Path.of(CONFIG_FILE_NAME));
    }

    public void loadConfig() throws IOException {
        if (!configFileExists()) {
            // Om du vill kan du slänga ett undantag eller skapa en tom fil här.
            throw new IOException("Konfigurationsfilen " + CONFIG_FILE_NAME + " saknas.");
        }
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_NAME)) {
            props.load(fis);
        }
    }

    public void saveConfig() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_NAME)) {
            props.store(fos, "Application Configuration");
        }
    }

    public record DatabaseLogin(String username, String password, String ip, String port) {}

    public DatabaseLogin getDatabaseLogin() throws IOException {
        if (!props.containsKey("db.username") ||
                !props.containsKey("db.password") ||
                !props.containsKey("db.ip")       ||
                !props.containsKey("db.port")) {
            throw new IOException("Konfigfilen saknar nödvändiga fält för databasen.");
        }

        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");
        String ip       = props.getProperty("db.ip");
        String port     = props.getProperty("db.port");

        return new DatabaseLogin(username, password, ip, port);
    }

    public void setDatabaseLogin(DatabaseLogin dbLogin) {
        props.setProperty("db.username", dbLogin.username());
        props.setProperty("db.password", dbLogin.password());
        props.setProperty("db.ip",       dbLogin.ip());
        props.setProperty("db.port",     dbLogin.port());
    }
}
