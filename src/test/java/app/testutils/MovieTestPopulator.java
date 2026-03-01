package app.testutils;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MovieTestPopulator {

    private MovieTestPopulator() {}

    public static Map<String, Movie> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            LocalDate baseDate = LocalDate.of(2028,2,1);
            Movie movie1 = new Movie(1L, "Movie 1", "good", baseDate.plusDays(1), 4.5, 1, "da");
            Movie movie2 = new Movie(2L, "Movie 2", "ok", baseDate.plusDays(2), 3.5, 2, "en");
            Movie movie3 = new Movie(3L, "Movie 3", "bad", baseDate.plusDays(3), 2.5, 3, "ja");

            try {
                em.createNativeQuery("TRUNCATE TABLE movie RESTART IDENTITY CASCADE").executeUpdate();
                em.persist(movie1);
                em.persist(movie2);
                em.persist(movie3);
                em.flush();
            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
            em.getTransaction().commit();

            Map<String, Movie> seeded = new LinkedHashMap<>();
            seeded.put("movie1", movie1);
            seeded.put("movie2", movie2);
            seeded.put("movie3", movie3);
            return seeded;
        }
    }
}