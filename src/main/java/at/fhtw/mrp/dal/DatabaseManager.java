package at.fhtw.mrp.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseManager {
    INSTANCE;

    public Connection getConnection()
    {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/",
                    "",
                    "");
        } catch (SQLException e) {
            throw new DataAccessException("Database connection could not be established", e);
        }
    }
}
