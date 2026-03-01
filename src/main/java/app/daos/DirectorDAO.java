package app.daos;

import app.entities.Director;
import jakarta.persistence.EntityManagerFactory;

public class DirectorDAO extends AbstractDAO<Director> {
    public DirectorDAO(EntityManagerFactory emf) {
        super(emf, Director.class);
    }
}