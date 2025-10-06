package at.fhtw.mrp.dal.repository;

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

    public Media createMedia(MediaCreationDto mediaCreationDto, Integer creatorId) {
        if (mediaCreationDto == null) {
            throw new NullPointerException("MediaCreationDto cannot be null");
        }
        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement(
                "INSERT INTO \"media\" (creatorId, title, description, releaseYear, averageScore, mediaType, ageRestriction) VALUES (?, ?, ?, ?, ?, ?, ?) return mediaId;"
        )) {
            preparedStatement.setInt(1, creatorId);
            preparedStatement.setString(2, mediaCreationDto.getTitle());
            preparedStatement.setString(3, mediaCreationDto.getDescription());
            preparedStatement.setDate(4, (Date) mediaCreationDto.getReleaseYear());
            preparedStatement.setFloat(5, 0); //averageScore is 0 at the beginning
            preparedStatement.setString(6, mediaCreationDto.getMediaType());
            preparedStatement.setInt(7, mediaCreationDto.getAgeRestriction());

            int result = preparedStatement.executeUpdate();
            if (result >= 1) {
                ArrayList<Integer> genreIds = null;
                try (GenreRepository genreRepository = new GenreRepository(new UnitOfWork())) {
                   genreIds = genreRepository.getGenreIds(mediaCreationDto.getGenres());
                }

                if (genreIds != null) {
                    Integer genreMediaInsertCount = addGenreRelations(result, genreIds);
                    if (genreMediaInsertCount == genreIds.size()) {
                        unitOfWork.commitTransaction();
                        unitOfWork.finishWork();
                    }
                }

                return getMedia(result);
            }

            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
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

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM \"media\" WHERE mediaId = ?")) {
            preparedStatement.setInt(1, mediaId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Media> mediaRows = new ArrayList<>();
            while(resultSet.next()) {
                Media media = new Media(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getDate(5),
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

        try (PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("DELETE FROM \"media\" WHERE mediaId = ?")) {
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

    private Integer addGenreRelations(Integer mediaId, ArrayList<Integer> genreIds) {
        StringBuilder sqlStatement = new StringBuilder("insert into mediaGenre (genreId, mediaId) values ");

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

            int y = 0;
            for(int x = 0; x < genreIds.size(); x++) {
                if(x == 0) {
                    preparedStatement.setInt(y, mediaId);
                    preparedStatement.setInt(y + 1, genreIds.get(x));
                }
                y = y + 2;
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            int size = 0;
            while (resultSet.next()) {
                size++;
            }
            return size;
        } catch (SQLException e) {
            this.unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.unitOfWork.rollbackTransaction();
        this.unitOfWork.close();
    }
}
