package at.fhtw.mrp.dal.repository;

import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.model.UserCreationDto;
import at.fhtw.mrp.utils.PasswordHashManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthRepository {
    private UnitOfWork unitOfWork;
    private PasswordHashManager passwordHashManager;

    public AuthRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public Boolean createUser(UserCreationDto user) throws SQLException {
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO users (username, password) VALUES (?)")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            Integer result = preparedStatement.executeUpdate();
            if (result >= 1) {
                unitOfWork.commitTransaction();
                unitOfWork.finishWork();
                return true;
            }

            return false;
        }
    }
}
