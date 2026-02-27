package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Movie {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    private LocalDate releaseDate;
    private double rating;
    private double popularity;
    private String originalLanguage;

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Actor> actors = new HashSet<>();

    @ManyToOne(cascade = CascadeType.MERGE)
    private Director director;

    public Movie(String title, String overview, LocalDate releaseDate, double rating, double popularity, String originalLanguage) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.popularity = popularity;
        this.originalLanguage = originalLanguage;
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}
