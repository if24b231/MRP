package at.fhtw.mrp.dal.repository;

import at.fhtw.mrp.dal.exceptions.DataAccessException;
import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepository {
    private UnitOfWork unitOfWork;

    public UserRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public Collection<User> getUser(String username) {
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                        select username, password from users where username = ?
                        """)
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        new User(
                                resultSet.getInt("userId"),
                                resultSet.getString("username"),
                                resultSet.getString("password"))
                );
            }

            return users;
        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException:", e);
        }
    }
}
