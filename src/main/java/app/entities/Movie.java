package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Movie {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private LocalDate releaseDate;

    private double rating;

    private double popularity;

    private String originalLanguage;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    public Movie(Long id,
                 String title,
                 String overview,
                 LocalDate releaseDate,
                 double rating,
                 double popularity,
                 String originalLanguage) {

        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.popularity = popularity;
        this.originalLanguage = originalLanguage;
    }
}