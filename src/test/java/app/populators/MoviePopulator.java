package app.populators;

import app.entities.Actor;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MoviePopulator {

    private MoviePopulator() {}

    public static Map<String, Movie> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            Movie movie1 = Movie.builder()
                    .title("In the Shadow Code")
                    .overview("A brilliant but reclusive software engineer discovers a hidden backdoor \n" +
                            "inside a global banking system. As he digs deeper, he realizes someone \n" +
                            "is manipulating world markets — and they know he’s watching.\n" +
                            "\"\"\"")
                    .releaseDate(LocalDate.of(2022, 10, 14))
                    .rating(7.8)
                    .originalLanguage("en")
                    .build();

            try {
                em.createNativeQuery("TRUNCATE TABLE movie RESTART IDENTITY CASCADE").executeUpdate();
                em.persist(movie1);
                em.flush();
            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
            em.getTransaction().commit();

            Map<String, Movie> seeded = new LinkedHashMap<>();
            seeded.put("movie1", movie1);
            return seeded;
        }
    }
}