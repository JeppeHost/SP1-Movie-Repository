package app.service;

import app.config.HibernateConfig;
import app.daos.*;
import app.dtos.*;
import app.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private final String apiKey = System.getenv("API_KEY");

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final MovieDAO movieDAO = new MovieDAO(emf);
    private final ActorDAO actorDAO = new ActorDAO(emf);
    private final GenreDAO genreDAO = new GenreDAO(emf);
    private final DirectorDAO directorDAO = new DirectorDAO(emf);

    public void fetchAndStoreDanishMoviesLast5Years() throws Exception {

        List<GenreDTO> apiGenres = fetchAllGenres();
        for (GenreDTO g : apiGenres) {
            if (genreDAO.findById(g.getId()) == null) {
                genreDAO.save(new Genre(g.getId(), g.getName()));
            }
        }

        int currentYear = Year.now().getValue();
        int startYear = currentYear - 5;

        int page = 1;
        String fiveYearsAgo = LocalDate.now().minusYears(5).toString();

        while (true) {

            String url = BASE_URL + "/discover/movie"
                    + "?api_key=" + apiKey
                    + "&with_original_language=da"
                    + "&primary_release_date.gte=" + startYear + "-01-01"
                    + "&primary_release_date.lte=" + currentYear + "-12-31"
                    + "&page=" + page;

            String json = fetch(url);
            MovieDTO.PageResult result = mapper.readValue(json, MovieDTO.PageResult.class);

            for (MovieDTO dto : result.getResults()) {
                processAndStoreMovie(dto);
            }

            if (page >= result.getTotalPages()) break;
            page++;
        }
    }

    private void processAndStoreMovie(MovieDTO dto) throws Exception {

        if (movieDAO.findById(dto.getId()) != null) {
            return;
        }

        Movie movie = new Movie(
                dto.getId(),
                dto.getTitle(),
                dto.getOverview(),
                dto.getReleaseDate(),
                dto.getVoteAverage(),
                dto.getPopularity(),
                dto.getOriginalLanguage()
        );

        for (Long genreId : dto.getGenreIds()) {
            Genre genre = genreDAO.findById(genreId);
            if (genre != null) {
                movie.getGenres().add(genre);
            }
        }

        CreditsDTO credits = fetchCredits(dto.getId());

        for (CreditsDTO.CastMemberDTO cast : credits.getCast()) {

            Actor actor = actorDAO.findById(cast.getId());
            if (actor == null) {
                actor = new Actor(cast.getId(), cast.getName());
                actorDAO.save(actor);
            }
            movie.getActors().add(actor);
        }

        for (CreditsDTO.CrewMemberDTO crew : credits.getCrew()) {
            if ("Director".equalsIgnoreCase(crew.getJob())) {

                Director director = directorDAO.findById(crew.getId());
                if (director == null) {
                    director = new Director(crew.getId(), crew.getName());
                    directorDAO.save(director);
                }
                movie.setDirector(director);
                break;
            }
        }

        movieDAO.save(movie);
    }

    private List<GenreDTO> fetchAllGenres() throws Exception {
        String json = fetch(BASE_URL + "/genre/movie/list?api_key=" + apiKey);
        GenreListResponse response = mapper.readValue(json, GenreListResponse.class);
        return response.getGenres();
    }

    private CreditsDTO fetchCredits(Long movieId) throws Exception {
        String json = fetch(BASE_URL + "/movie/" + movieId + "/credits?api_key=" + apiKey);
        return mapper.readValue(json, CreditsDTO.class);
    }

    private String fetch(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public List<Movie> getAllMovies() {
        return movieDAO.findAll();
    }

    public List<Actor> getAllActors() {
        return actorDAO.findAll();
    }

    public List<Director> getAllDirectors() {
        return directorDAO.findAll();
    }

    public List<Genre> getAllGenres() {
        return genreDAO.findAll();
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movieDAO.searchByTitle(title);
    }

    public List<Movie> getMoviesByGenre(Long genreId) {
        return movieDAO.findByGenreId(genreId);
    }

    public double getAverageRating() {
        return movieDAO.getAverageRating();
    }

    public List<Movie> getTop10HighestRated() {
        return movieDAO.getTop10HighestRated();
    }

    public List<Movie> getTop10LowestRated() {
        return movieDAO.getTop10LowestRated();
    }

    public List<Movie> getTop10MostPopular() {
        return movieDAO.getTop10MostPopular();
    }

    public void deleteMovie(Long id) {
        movieDAO.delete(id);
    }
}