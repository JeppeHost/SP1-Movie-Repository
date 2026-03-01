package app.daos;

import app.GenreDAO;
import app.config.HibernateConfig;
import app.entities.Genre;
import app.populators.GenrePopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreDAOTest {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private GenreDAO genreDAO;

    private Map<String, Genre> seeded;

    @BeforeEach
    void setUp() {
        seeded = GenrePopulator.populate(emf);
        genreDAO = new GenreDAO(emf);
    }


    @AfterAll
    void shutDown() {
        emf.close();
    }

    @Test
    void findById() {

        //Arrange
        Genre fetched;

        //Act
        fetched = genreDAO.findById(1);

        //Assert
        assertThat(fetched, notNullValue());
        assertThat(fetched.getId(), is(1));
    }

    @Test
    void findAll() {

        //Arrange
        List<Genre> allGenres;

        //Act
        allGenres = genreDAO.findAll();

        //Assert
        assertThat(allGenres, notNullValue());
        assertThat(allGenres.size(), is(seeded.size()));
    }

    @Test
    void save() {
        //Arrange
        Genre genre = new Genre("True Crime");

        //Act
        Genre created = genreDAO.save(genre);

        //Assert

        assertThat(created, notNullValue());
        assertThat(created.getName(), is("True Crime"));
    }

    @Test
    void update() {
        // Arrange
        Genre genre = new Genre("Science Fiction");
        Genre created = genreDAO.save(genre);
        int id = created.getId();

        // Act
        created.setName("Thriller");
        Genre updatedGenre = genreDAO.update(created);

        // Assert
        assertThat(updatedGenre, notNullValue());
        assertThat(updatedGenre.getName(), is("Thriller"));
        assertThat(updatedGenre.getId(), is(id));
    }

    @Test
    void delete() {
        //Arrange

        //Act
        genreDAO.delete(1);

        //Assert
        assertThat(genreDAO.findById(1), is(nullValue()));
    }
}