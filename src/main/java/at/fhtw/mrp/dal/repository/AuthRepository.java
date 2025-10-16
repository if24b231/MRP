package at.fhtw.mrp.dal.repository;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.User;
import at.fhtw.mrp.dal.exceptions.UserAlreadyExistsException;
import at.fhtw.mrp.model.UserCreationDto;
import at.fhtw.mrp.utils.PasswordHashManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class AuthRepository implements AutoCloseable {
    private final UnitOfWork unitOfWork;
    private PasswordHashManager passwordHashManager;

    public AuthRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public void createUser(UserCreationDto user) throws SQLException {
        if (getUser(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("User already exists");
        }


        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(
                "INSERT INTO \"user\" (username, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            int result = preparedStatement.executeUpdate();
            if (result != 1) {
                throw new SQLException("Failed to create user");
            }
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            this.unitOfWork.finishWork();
            Logger.log(LogType.ERROR, e.getLocalizedMessage());
        }
    }

    public User getUser(String username) {
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM \"user\" WHERE username = ?")) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            Collection<User> userRows = new ArrayList<>();
            while(resultSet.next()) {
                User user = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                userRows.add(user);
            }

            if (userRows.isEmpty()) {
                return null;
            }

            return userRows.stream().findFirst().orElse(null);
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }
    }

    public void SaveChanges() {
        this.unitOfWork.commitTransaction();
        this.unitOfWork.finishWork();
    }

    @Override
    public void close() throws Exception {
        this.unitOfWork.finishWork();
    }
}
