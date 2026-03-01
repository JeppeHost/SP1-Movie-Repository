package app.daos;

import app.config.HibernateConfig;
import app.entities.Movie;
import app.exceptions.ApiException;
import app.testutils.MovieTestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieDAOTest {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();

    private MovieDAO movieDAO;
    private Map<String, Movie> seeded;

    @BeforeEach
    void beforeEach() {
        seeded = MovieTestPopulator.populate(emf);
        movieDAO = new MovieDAO(emf);
    }

    @AfterAll
    void shutdown() {
        emf.close();
    }

    @Test
    void findById() {
        Movie seed = seeded.get("movie1");
        Movie fetched = movieDAO.findById(seed.getId());
        assertEquals(seed.getId(), fetched.getId());
        assertEquals(seed.getTitle(), fetched.getTitle());
    }

    @Test
    void findAll() {
        List<Movie> all = movieDAO.findAll();
        assertEquals(seeded.get("movie1"), all.get(0));
        assertEquals(seeded.get("movie2"), all.get(1));
        assertEquals(seeded.get("movie3"), all.get(2));
    }

    @Test
    void save() {
        Movie movie = new Movie(4L, "Movie 4", "mid", LocalDate.now(), 3.0, 4, "da");

        Movie created = movieDAO.save(movie);

        assertNotNull(created.getId());
        Movie fetched = movieDAO.findById(created.getId());
        assertEquals("Movie 4", fetched.getTitle());
        assertEquals("mid", fetched.getOverview());
        assertEquals(LocalDate.now(), fetched.getReleaseDate());
        assertEquals(3, fetched.getRating());
        assertEquals(4, fetched.getPopularity());
        assertEquals("da", fetched.getOriginalLanguage());
    }

    @Test
    void update() {
        Movie seed = seeded.get("movie2");
        seed.setTitle("Updated Title");
        movieDAO.update(seed);

        assertEquals("Updated Title", movieDAO.findById(2L).getTitle());
    }

    @Test
    void delete() {
        Movie seed = seeded.get("movie3");

        movieDAO.delete(seed.getId());

        assertNull(movieDAO.findById(3L));
        assertThrows(ApiException.class, () -> movieDAO.findById(seed.getId()));
    }

    @Test
    void searchByTitle() {
        Movie seed = seeded.get("movie1");
        List<Movie> fetched = movieDAO.searchByTitle("1");
        assertEquals(seed, fetched.get(0));
        assertNull(fetched.get(1));
    }

    @Test
    void getAverageRating() {
        assertEquals(3.5, movieDAO.getAverageRating());
    }

    @Test
    void getTop10HighestRated() {
        assertEquals(seeded.get("movie1"), movieDAO.getTop10HighestRated().get(0));
        assertEquals(seeded.get("movie2"), movieDAO.getTop10HighestRated().get(1));
        assertEquals(seeded.get("movie3"), movieDAO.getTop10HighestRated().get(2));
    }

    @Test
    void getTop10LowestRated() {
        assertEquals(seeded.get("movie3"), movieDAO.getTop10LowestRated().get(0));
        assertEquals(seeded.get("movie2"), movieDAO.getTop10LowestRated().get(1));
        assertEquals(seeded.get("movie1"), movieDAO.getTop10LowestRated().get(2));
    }

    @Test
    void getTop10MostPopular() {
        assertEquals(seeded.get("movie3"), movieDAO.getTop10MostPopular().get(2));
        assertEquals(seeded.get("movie2"), movieDAO.getTop10MostPopular().get(1));
        assertEquals(seeded.get("movie1"), movieDAO.getTop10MostPopular().get(0));
    }
}