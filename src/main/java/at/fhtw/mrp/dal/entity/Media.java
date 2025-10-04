package at.fhtw.mrp.dal.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Getter
public class Media {
    private final Integer mediaId;
    private final Integer creatorId;
    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private Date releaseYear;
    @Setter
    private BigDecimal averageScore;
    @Setter
    private String mediaType;
    @Setter
    private Integer ageRestriction;
    @Setter
    private ArrayList<Genre> genre;

    public Media(Integer mediaId, Integer creatorId, String title, String description, Date releaseYear, BigDecimal averageScore, String mediaType, Integer ageRestriction,  ArrayList<Genre> genre) {
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
