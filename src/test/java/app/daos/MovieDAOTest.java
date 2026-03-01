package app.daos;

import app.config.HibernateConfig;
import app.entities.Movie;
import app.exceptions.ApiException;
import app.testutils.MovieTestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.testcontainers.shaded.org.hamcrest.Matchers.containsInAnyOrder;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;


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
        assertThat(fetched.getId(), is(seed.getId()));
        assertThat(fetched.getTitle(), is(seed.getTitle()));
    }

    @Test
    void findAll() {
        List<Movie> all = movieDAO.findAll();
        assertThat(all, hasSize(3));
        assertThat(all, containsInAnyOrder(seeded.get("movie1"), seeded.get("movie2"), seeded.get("movie3")));
    }

    @Test
    void save() {
        Movie movie = new Movie(4L, "Movie 4", "mid", LocalDate.now(), 3.0, 400, "da");

        Movie created = movieDAO.save(movie);

        assertThat(created.getId(), notNullValue());
        Movie fetched = movieDAO.findById(created.getId());
        assertThat(fetched.getId(), is(4));
        assertThat(fetched.getTitle(), is("Movie 4"));
        assertThat(fetched.getOverview(), is("mid"));
        assertThat(fetched.getReleaseDate(), is(LocalDate.now()));
        assertThat(fetched.getRating(), is(3));
        assertThat(fetched.getPopularity(), is(400));
        assertThat(fetched.getOriginalLanguage(), is("da"));
    }

    @Test
    void update() {
        Movie seed = seeded.get("movie2");
        seed.setTitle("Updated Title");
        movieDAO.update(seed);

        assertThat(movieDAO.findById(2L).getTitle(), is("Updated Title"));
    }

    @Test
    void delete() {
        Movie seed = seeded.get("movie3");

        movieDAO.delete(seed.getId());

        assertThrows(ApiException.class, () -> movieDAO.findById(seed.getId()));
    }

    @Test
    void searchByTitle() {
    }

    @Test
    void getAverageRating() {
    }

    @Test
    void getTop10HighestRated() {
    }

    @Test
    void getTop10LowestRated() {
    }

    @Test
    void getTop10MostPopular() {
    }

    @Test
    void findByGenreId() {
    }
}