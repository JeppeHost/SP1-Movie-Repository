package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL)
    private Set<Movie> movie;

    public Director(String name) {
        this.name = name;
    }

    public void addMovie(Movie movie) {
        this.movie.add(movie);
        if (movie != null) {
            movie.setDirector(this);
        }
    }
}
