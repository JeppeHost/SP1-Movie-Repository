package app.populators;

import app.entities.Director;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import jakarta.persistence.PersistenceException;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DirectorPopulator {

    private DirectorPopulator() {}

    public static Map<String, Director> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            Director director1 = new Director("Martin Scorsese");

            try {
                em.createNativeQuery("TRUNCATE TABLE director RESTART IDENTITY CASCADE").executeUpdate();
                em.persist(director1);
                em.flush();
            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
            em.getTransaction().commit();

            Map<String, Director> seeded = new LinkedHashMap<>();
            seeded.put("director1", director1);
            return seeded;
        }
    }
}