package org.grupp2.sdpproject.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DatabaseLoginManager {

    private static final String CONFIG_FILE_NAME = "db_login.properties";

    public record DatabaseLogin(String username, String password, String ip, String port) {}

    public static boolean configFileExists() {
        return Files.exists(Path.of(CONFIG_FILE_NAME));
    }

    public static DatabaseLogin readConfigFromFile() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_NAME)) {
            props.load(fis);
        }

        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");
        String ip       = props.getProperty("db.ip");
        String port     = props.getProperty("db.port");

        if (username == null || password == null || ip == null || port == null) {
            throw new IOException("Konfigfilen saknar nödvändiga fält.");
        }

        return new DatabaseLogin(username, password, ip, port);
    }

    public static void writeConfigToFile(DatabaseLogin config) throws IOException {
        Properties props = new Properties();
        props.setProperty("db.username", config.username());
        props.setProperty("db.password", config.password());
        props.setProperty("db.ip",       config.ip());
        props.setProperty("db.port",     config.port());

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_NAME)) {
            props.store(fos, "Database configuration");
        }
    }
}
