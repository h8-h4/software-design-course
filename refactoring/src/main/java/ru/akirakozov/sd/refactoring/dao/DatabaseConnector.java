package ru.akirakozov.sd.refactoring.dao;


import ru.akirakozov.sd.refactoring.config.ConfigProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DATABASE_URL = ConfigProvider.dbConfig().url();

    private DatabaseConnector() {
    }

    public static Connection newConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
