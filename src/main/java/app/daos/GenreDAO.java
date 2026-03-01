package app.daos;

import app.entities.Genre;
import jakarta.persistence.EntityManagerFactory;

public class GenreDAO extends AbstractDAO<Genre> {
    public GenreDAO(EntityManagerFactory emf) {
        super(emf, Genre.class);
    }
}