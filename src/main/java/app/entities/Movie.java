package app.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity

public class Movie {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private double rating;
    private Set<Genre> genres;
    private Set<Actor> actors;
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;
}
