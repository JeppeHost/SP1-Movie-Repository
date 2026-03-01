package app.daos;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class MovieDAO extends AbstractDAO<Movie> {

    public MovieDAO(EntityManagerFactory emf) {
        super(emf, Movie.class);
    }

    public List<Movie> searchByTitle(String title) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:title)", Movie.class)
                    .setParameter("title", "%" + title + "%")
                    .getResultList();
        }
    }

    public double getAverageRating() {
        try (EntityManager em = emf.createEntityManager()) {
            Double result = em.createQuery("SELECT AVG(m.rating) FROM Movie m", Double.class).getSingleResult();
            return result != null ? result : 0.0;
        }
    }

    public List<Movie> getTop10HighestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.rating DESC", Movie.class)
                    .setMaxResults(10).getResultList();
        }
    }

    public List<Movie> getTop10LowestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.rating ASC", Movie.class)
                    .setMaxResults(10).getResultList();
        }
    }

    public List<Movie> getTop10MostPopular() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.popularity DESC", Movie.class)
                    .setMaxResults(10).getResultList();
        }
    }

    public List<Movie> findByGenreId(Long genreId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :id", Movie.class)
                    .setParameter("id", genreId)
                    .getResultList();
        }
    }
}