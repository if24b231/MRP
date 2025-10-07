package at.fhtw.mrp.dal.repository;

import at.fhtw.Logging.LogType;
import at.fhtw.Logging.Logger;
import at.fhtw.mrp.dal.UnitOfWork;
import at.fhtw.mrp.dal.entity.Genre;
import at.fhtw.mrp.dal.entity.Media;
import at.fhtw.mrp.dal.exceptions.ForbiddenException;
import at.fhtw.mrp.dal.exceptions.NotFoundException;
import at.fhtw.mrp.model.MediaCreationDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MediaRepository implements AutoCloseable {
    private final UnitOfWork unitOfWork;

    public MediaRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public Integer createMedia(MediaCreationDto mediaCreationDto, Integer creatorId) {
        if (mediaCreationDto == null) {
            throw new NullPointerException("MediaCreationDto cannot be null");
        }
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(
                "INSERT INTO \"media\" (\"creatorId\", title, description, \"releaseYear\", \"averageScore\", \"mediaType\", \"ageRestriction\") VALUES (?, ?, ?, ?, ?, ?, ?) returning \"mediaId\";"
        )) {
            preparedStatement.setInt(1, creatorId);
            preparedStatement.setString(2, mediaCreationDto.getTitle());
            preparedStatement.setString(3, mediaCreationDto.getDescription());
            preparedStatement.setInt(4, mediaCreationDto.getReleaseYear());
            preparedStatement.setFloat(5, 0); //averageScore is 0 at the beginning
            preparedStatement.setString(6, mediaCreationDto.getMediaType());
            preparedStatement.setInt(7, mediaCreationDto.getAgeRestriction());

            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                ArrayList<Integer> genreIds = null;
                try (GenreRepository genreRepository = new GenreRepository(new UnitOfWork())) {
                   genreIds = genreRepository.getGenreIds(mediaCreationDto.getGenres());
                }

                addGenreRelations(result.getInt(1), genreIds);
                this.unitOfWork.commitTransaction();
                this.unitOfWork.finishWork();
                return result.getInt(1);
            }
        } catch (Exception e) {
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Media getMedia(Integer mediaId) {
        ArrayList<Genre> genresOfMedia = null;
        try(GenreRepository genreRepository = new GenreRepository(new UnitOfWork())) {
            genresOfMedia = genreRepository.getGenresOfMedia(mediaId);
        } catch (Exception e) {
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM \"media\" WHERE \"mediaId\" = ?;")) {
            preparedStatement.setInt(1, mediaId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Media> mediaRows = new ArrayList<>();
            while(resultSet.next()) {
                Media media = new Media(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getFloat(6),
                        resultSet.getString(7),
                        resultSet.getInt(8),
                        genresOfMedia
                );
                mediaRows.add(media);
            }

            unitOfWork.commitTransaction();
            unitOfWork.finishWork();

            if (mediaRows.isEmpty()) {
                throw new NotFoundException("Media not found");
            }

            return mediaRows.getFirst();
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }
    }

    public void deleteMedia(Integer mediaId, Integer creatorId) {
        Media mediaToDelete = getMedia(mediaId);
        if(mediaToDelete == null) {
            throw new NotFoundException("Media not found");
        }

        if(!mediaToDelete.getCreatorId().equals(creatorId)) {
            throw new ForbiddenException("User is not the Creator of this media");
        }

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("DELETE FROM \"media\" WHERE mediaId = ?;")) {
            preparedStatement.setInt(1, mediaId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                unitOfWork.commitTransaction();
                unitOfWork.finishWork();
            }
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }
    }

    private void addGenreRelations(Integer mediaId, ArrayList<Integer> genreIds) {
        StringBuilder sqlStatement = new StringBuilder("insert into \"mediaGenre\" (\"mediaId\", \"genreId\") values ");

        int i = 0;
        for(Integer genreId : genreIds) {
            if(genreIds.size() > i + 1 ) {
                sqlStatement.append("(?,?)").append(",");
                i++;
            } else {
                sqlStatement.append("(?,?)").append(";");
            }
        }

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(sqlStatement.toString())) {

            int y = 1;
            for(int x = 0; x < genreIds.size(); x++) {
                preparedStatement.setInt(y, mediaId);
                preparedStatement.setInt(y + 1, genreIds.get(x));
                y = y + 2;
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.log(LogType.ERROR, e.getLocalizedMessage());
            this.unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.unitOfWork.finishWork();
    }
}
