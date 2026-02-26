package app.daos;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class MovieDAO implements IDAO<Movie> {

    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Movie findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Movie.class, id);
        }
    }

    @Override
    public List<Movie> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        }
    }

    @Override
    public Movie save(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        }
    }

    @Override
    public Movie update(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie updated = em.merge(movie);
            em.getTransaction().commit();
            return updated;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            if (movie != null) em.remove(movie);
            em.getTransaction().commit();
        }
    }

    public List<Movie> findByRating(double minRating, double maxRating) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Movie m WHERE m.voteAverage >= :min AND m.voteAverage <= :max",
                            Movie.class)
                    .setParameter("min", minRating)
                    .setParameter("max", maxRating)
                    .getResultList();
        }
    }

    public List<Movie> findByGenre(String genreName) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT m FROM Movie m JOIN m.genres g WHERE g.name = :name",
                            Movie.class)
                    .setParameter("name", genreName)
                    .getResultList();
        }
    }
}