package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class MediaCreationDto {
    @NonNull
    private String title;
    @JsonAlias({"title"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @NonNull
    private String description;
    @JsonAlias({"releaseYear"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @NonNull
    private Integer releaseYear;
    @JsonAlias({"mediaType"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @NonNull
    private String mediaType;
    @JsonAlias({"ageRestriction"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @NonNull
    private Integer ageRestriction;
    @JsonAlias({"genres"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private ArrayList<String> genres;
}

