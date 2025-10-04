package at.fhtw.mrp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Getter @Setter @AllArgsConstructor
public class MediaCreationDto {
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private Date releaseYear;
    @NonNull
    private BigDecimal averageScore;
    @NonNull
    private String mediaType;
    @NonNull
    private Integer ageRestriction;
    private ArrayList<String> genre;
}

