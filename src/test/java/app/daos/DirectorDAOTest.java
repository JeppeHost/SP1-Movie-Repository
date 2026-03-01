package app.daos;


import app.DirectorDAO;
import app.config.HibernateConfig;
import app.entities.Director;
import app.populators.DirectorPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorDAOTest {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private DirectorDAO directorDAO;

    private Map<String, Director> seeded;

    @BeforeEach
    void setUp() {
        seeded = DirectorPopulator.populate(emf);
        directorDAO = new DirectorDAO(emf);
    }


    @AfterAll
    void shutDown() {
        emf.close();
    }

    @Test
    void findById() {
        //Arrange
        Director fetched;

        //Act
        fetched = directorDAO.findById(1);

        //Assert
        assertThat(fetched, notNullValue());
        assertThat(fetched.getId(), is(1));
    }

    @Test
    void findAll() {
        //Arrange
        List<Director> allDirectors;

        //Act
        allDirectors = directorDAO.findAll();

        //Assert
        assertThat(allDirectors, notNullValue());
        assertThat(allDirectors.size(), is(seeded.size()));
    }

    @Test
    void save() {
        //Arrange
        Director director1 = new Director("Christopher Nolan");

        //Act
        Director created = directorDAO.save(director1);

        //Assert

        assertThat(created, notNullValue());
        assertThat(created.getName(), is("Christopher Nolan"));
    }

    @Test
    void update() {
        // Arrange
        Director director = new Director("Anders Thomas Jensen");
        Director created = directorDAO.save(director);
        int id = created.getId();

        // Act
        created.setName("Thomas Vinterberg");
        Director updatedDirector = directorDAO.update(created);

        // Assert
        assertThat(updatedDirector, notNullValue());
        assertThat(updatedDirector.getName(), is("Thomas Vinterberg"));
        assertThat(updatedDirector.getId(), is(id));
    }

    @Test
    void delete() {

        //Arrange

        //Act
        directorDAO.delete(1);

        //Assert
        assertThat(directorDAO.findById(1), is(nullValue()));
    }
}