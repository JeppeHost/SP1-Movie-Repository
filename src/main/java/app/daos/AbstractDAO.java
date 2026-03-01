package app.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public abstract class AbstractDAO<T> implements IDAO<T> {

    protected final EntityManagerFactory emf;
    private final Class<T> entityClass;

    public AbstractDAO(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }

    @Override
    public T findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
        }
    }

    @Override
    public T save(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                em.persist(entity);
                em.getTransaction().commit();
                return entity;
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public T update(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                T updated = em.merge(entity);
                em.getTransaction().commit();
                return updated;
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                T entity = em.find(entityClass, id);
                if (entity != null) em.remove(entity);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }
}