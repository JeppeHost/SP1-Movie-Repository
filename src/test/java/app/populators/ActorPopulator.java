package app.populators;

import app.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import jakarta.persistence.PersistenceException;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ActorPopulator {

    private ActorPopulator() {}

    public static Map<String, Actor> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            Actor actor1 = new Actor("Tom Cruise");

            try {
                em.createNativeQuery("TRUNCATE TABLE actor RESTART IDENTITY CASCADE").executeUpdate();
                em.persist(actor1);
                em.flush();
            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
            em.getTransaction().commit();

            Map<String, Actor> seeded = new LinkedHashMap<>();
            seeded.put("actor1", actor1);
            return seeded;
        }
    }
}