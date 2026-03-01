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
@AllArgsConstructor
@Builder

public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private LocalDate releaseDate;
    private double rating;
    private String originalLanguage;

    @ManyToMany
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    private Set<Actor> actors  = new HashSet<>();

    @ManyToOne
    private Director director;

    public Movie(String title, String overview, LocalDate releaseDate, double rating, String originalLanguage) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.originalLanguage = originalLanguage;
    }


    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}
