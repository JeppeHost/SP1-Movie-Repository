package app.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = new Configuration()
                    .addAnnotatedClass(app.entities.Movie.class)
                    .addAnnotatedClass(app.entities.Actor.class)
                    .addAnnotatedClass(app.entities.Genre.class)
                    .addAnnotatedClass(app.entities.Director.class)
                    .setProperty("hibernate.connection.url", System.getenv("DB_URL"))
                    .setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"))
                    .setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"))
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .buildSessionFactory();
        }
        return emf;
    }

    public static void close() {
        if (emf != null) emf.close();
    }
}