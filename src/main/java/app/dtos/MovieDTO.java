package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {

    private Long id;
    private String title;
    private String overview;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("original_language")
    private String originalLanguage;

    private List<app.dtos.GenreDTO> genres;

    private List<ActorDTO> cast;

    private app.dtos.DirectorDTO director;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageResult {
        private List<MovieDTO> results;

        @JsonProperty("total_pages")
        private int totalPages;
    }
}