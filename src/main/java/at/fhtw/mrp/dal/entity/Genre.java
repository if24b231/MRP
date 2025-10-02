package at.fhtw.mrp.dal.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class Genre {
    private final Integer genreId;
    @Setter
    private String name;

    public Genre(@NonNull Integer genreId, @NonNull String name) {
        this.genreId = genreId;
        this.name = name;
    }
}
