package at.fhtw.mrp.dal.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class Media {
    private final Integer mediaId;
    private final Integer creatorId;
    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private Integer releaseYear;
    @Setter
    private Float averageScore;
    @Setter
    private String mediaType;
    @Setter
    private Integer ageRestriction;
    @Setter
    private ArrayList<Genre> genre;

    public Media(Integer mediaId, Integer creatorId, String title, String description, Integer releaseYear, Float averageScore, String mediaType, Integer ageRestriction,  ArrayList<Genre> genre) {
        this.mediaId = mediaId;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.averageScore = averageScore;
        this.mediaType = mediaType;
        this.ageRestriction = ageRestriction;
        this.genre = genre;
    }
}
