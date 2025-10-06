package at.fhtw.mrp.dal.repository;

import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.Genre;
import at.fhtw.mrp.dal.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenreRepository implements AutoCloseable {
    private final UnitOfWork unitOfWork;

    public GenreRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public ArrayList<Integer> getGenreIds(List<String> genreNames) {
        StringBuilder sqlStatement = new StringBuilder("SELECT genreId FROM \"genre\" WHERE name in (");

        int i = 0;
        for(String genreName : genreNames) {
            if(genreName.length() > i + 1 ) {
                sqlStatement.append("?").append(",");
                i++;
            } else {
                sqlStatement.append("?").append(");");
            }
        }

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(sqlStatement.toString())) {

            for(int x = 0; x < genreNames.size(); x++) {
                preparedStatement.setString(x, genreNames.get(x));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            Collection<Integer> genreRows = new ArrayList<>();
            while (resultSet.next()) {
                genreRows.add(resultSet.getInt(0));
            }

            unitOfWork.commitTransaction();
            unitOfWork.finishWork();

            if (genreRows.isEmpty()) {
                return null;
            }

            return new ArrayList<>(genreRows);
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        this.unitOfWork.rollbackTransaction();
        this.unitOfWork.close();
    }

    public ArrayList<Genre> getGenresOfMedia(Integer mediaId) {
        if(mediaId == null) {
            return null;
        }

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM \"mediaGenre\" WHERE mediaId = ?")) {
            preparedStatement.setInt(1, mediaId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Genre> genreRows = new ArrayList<>();
            while(resultSet.next()) {
                Genre genre = new Genre(
                        resultSet.getInt(1),
                        resultSet.getString(2));
                genreRows.add(genre);
            }

            unitOfWork.commitTransaction();
            unitOfWork.finishWork();

            if (genreRows.isEmpty()) {
                return null;
            }

            return genreRows;
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }
    }
}
