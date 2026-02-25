package app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity

public class Actor {
    @Id
    @GeneratedValue
    private int id;
    private String name;
}
