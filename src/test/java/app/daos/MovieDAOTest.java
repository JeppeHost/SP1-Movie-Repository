package app.daos;


import app.MovieDAO;
import app.config.HibernateConfig;
import app.entities.Movie;
import app.populators.MoviePopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieDAOTest {


    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private MovieDAO movieDAO;

    private Map<String, Movie> seeded;



    @BeforeEach
    void setUp() {

        seeded = MoviePopulator.populate(emf);
        movieDAO = new MovieDAO(emf);

    }

    @AfterAll
    void shutDown() {
        emf.close();

    }

    @Test
    void findById() {
        //Arrange
        Movie fetched;

        //Act
        fetched = movieDAO.findById(1);

        //Assert
        assertThat(fetched, notNullValue());
        assertThat(fetched.getId(), is(1));
        assertThat(fetched.getTitle(), is("In the Shadow Code"));

    }

    @Test
    void findAll() {

        //Arrange
        List<Movie> allMovies;

        //Act
        allMovies = movieDAO.findAll();

        //Assert
        assertThat(allMovies.size(), is(seeded.size()));

    }

    @Test
    void save() {
        //Arrange
        Movie movie2 = Movie.builder()
                .title("Red Horizon")
                .overview("""
After a failed Mars mission leaves one astronaut stranded, 
she must survive using limited resources while mission control 
on Earth races against time to bring her home.
""")
                .releaseDate(LocalDate.of(2021, 5, 6))
                .rating(8.2)
                .originalLanguage("en")
                .build();

        //Act
        Movie createdMovie = movieDAO.save(movie2);


        //Assert
        assertThat(createdMovie.getId(), is(2));
        assertThat(createdMovie.getTitle(), is("Red Horizon"));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        //Arrange

        //Act
        movieDAO.delete(1);

        //Assert
        assertThat(seeded.size(), is(1));
        assertThat(movieDAO.findById(1), nullValue());
    }

    @Test
    void findByRating() {
    }

    @Test
    void findByGenre() {
    }
}