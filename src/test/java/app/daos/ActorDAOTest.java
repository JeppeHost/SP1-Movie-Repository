package app.daos;

import app.config.HibernateConfig;
import app.ActorDAO;
import app.populators.ActorPopulator;
import app.entities.Actor;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActorDAOTest {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private ActorDAO actorDAO;

    private Map<String, Actor> seeded;


    @BeforeEach
    void setUp() {
        seeded = ActorPopulator.populate(emf);
        actorDAO = new ActorDAO(emf);
    }


    @AfterAll
    void shutDown() {
        emf.close();
    }

    @Test
    void findById() {

        //Arrange
        Actor fetched;

        //Act
        fetched = actorDAO.findById(1);

        //Assert
        assertThat(fetched, notNullValue());
        assertThat(fetched.getId(), is(1));
    }

    @Test
    void findAll() {

        //Arrange
        List<Actor> allActors;

        //Act
        allActors = actorDAO.findAll();

        //Assert
        assertThat(allActors, notNullValue());
        assertThat(allActors.size(), is(seeded.size()));

    }

    @Test
    void save() {
        //Arrange
        Actor actor1 = new Actor("Dwayne Johnson");

        //Act
        Actor created = actorDAO.save(actor1);

        //Assert

        assertThat(created, notNullValue());
        assertThat(created.getName(), is("Dwayne Johnson"));
    }

    @Test
    void update() {

        //Arrange
        Actor actorToUpdate = new Actor("Tom Hanks");

        //Act
        actorToUpdate = actorDAO.update(actorToUpdate);

        //Assert

    }

    @Test
    void delete() {

        //Arrange

        //Act
        actorDAO.delete(1);

        //Assert
        assertThat(actorDAO.findById(1), is(nullValue()));
    }
}