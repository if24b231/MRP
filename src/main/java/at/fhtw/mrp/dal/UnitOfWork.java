package at.fhtw.mrp.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UnitOfWork implements AutoCloseable {
    private Connection connection;

    public UnitOfWork() {
        this.connection = DatabaseManager.INSTANCE.getConnection();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DataAccessException("Autocommit not disableable", e);
        }
    }

    public void commitTransaction()
    {
        if (this.connection != null) {
            try {
                this.connection.commit();
            } catch (SQLException e) {
                throw new DataAccessException("Commit not successful", e);
            }
        }
    }

    public void rollbackTransaction()
    {
        if (this.connection != null) {
            try {
                this.connection.rollback();
            } catch (SQLException e) {
                throw new DataAccessException("Rollback not successful", e);
            }
        }
    }

    public void finishWork()
    {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                throw new DataAccessException("Connection could not be closed", e);
            }
        }
    }

    public PreparedStatement prepareStatement(String sql)
    {
        if (this.connection != null) {
            try {
                return this.connection.prepareStatement(sql);
            } catch (SQLException e) {
                throw new DataAccessException("PreparedStatement could not be created", e);
            }
        }
        throw new DataAccessException("UnitOfWork has no available connection");
    }

    @Override
    public void close() throws Exception {
        this.finishWork();
    }
}
