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
            em.merge(movie);
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

    public List<Movie> searchByTitle(String title) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:title)", Movie.class)
                    .setParameter("title", "%" + title + "%")
                    .getResultList();
            return movies;
        }
    }

    public double getAverageRating() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT AVG(m.rating) FROM Movie m", Double.class)
                    .getSingleResult();
        }
    }

    public List<Movie> getTop10HighestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.rating DESC", Movie.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }

    public List<Movie> getTop10LowestRated() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.rating ASC", Movie.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }

    public List<Movie> getTop10MostPopular() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.popularity DESC", Movie.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }

    public List<Movie> findByGenre(String genreName) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Movie> movies = em.createQuery(
                            "SELECT m FROM Movie m JOIN m.genres g WHERE g.name = :name", Movie.class)
                    .setParameter("name", genreName)
                    .getResultList();
            return movies;
        }
    }
}