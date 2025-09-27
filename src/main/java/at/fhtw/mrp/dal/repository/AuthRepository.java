package at.fhtw.mrp.dal.repository;

import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.User;
import at.fhtw.mrp.dal.exceptions.UserAlreadyExistsException;
import at.fhtw.mrp.model.UserCreationDto;
import at.fhtw.mrp.utils.PasswordHashManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class AuthRepository implements AutoCloseable {
    private UnitOfWork unitOfWork;
    private PasswordHashManager passwordHashManager;

    public AuthRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public Boolean createUser(UserCreationDto user) throws SQLException {
        if (getUser(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("User already exists");
        }


        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(
                "INSERT INTO \"user\" (username, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                unitOfWork.commitTransaction();
                unitOfWork.finishWork();
                return true;
            }

            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            return false;
        }
    }

    public User getUser(String username) throws SQLException {
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
        }
    }

    @Override
    public void close() throws Exception {

    }
}
