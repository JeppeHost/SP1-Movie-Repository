package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Director {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "director")
    @ToString.Exclude
    private Set<Movie> movies = new HashSet<>();

    public Director(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}