package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Actor {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies = new HashSet<>();

    public Actor(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}