/*package app.service;

import app.config.HibernateConfig;
import app.ActorDAO;
import app.DirectorDAO;
import app.GenreDAO;
import app.MovieDAO;
import app.dtos.ActorDTO;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private final String apiKey = System.getenv("API_KEY");
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final MovieDAO movieDAO = new MovieDAO(emf);
    private final ActorDAO actorDAO = new ActorDAO(emf);
    private final GenreDAO genreDAO = new GenreDAO(emf);
    private final DirectorDAO directorDAO = new DirectorDAO(emf);

    public MovieDTO getMovieById(Long id) throws Exception {
        String json = fetch(BASE_URL + "/movie/" + id + "?api_key=" + apiKey);
        return objectMapper.readValue(json, MovieDTO.class);
    }

    public List<MovieDTO> getDanishMovies() throws Exception {
        List<MovieDTO> all = new ArrayList<>();
        int page = 1;

        while (true) {
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("API_KEY environment variable mangler. Sæt den i IntelliJ Run Config.");
            }
            String json = fetch(BASE_URL + "/discover/movie?api_key=" + apiKey
                    + "&with_original_language=da&page=" + page);
            MovieDTO.PageResult response = objectMapper.readValue(json, MovieDTO.PageResult.class);
            if (response.getResults() == null) {
                throw new RuntimeException("TMDB response havde ingen 'results' (var det en error JSON?). Side: " + page);
            }
            all.addAll(response.getResults());
            if (page >= response.getTotalPages()) break;
            page++;
        }
        return all;
    }

    public void fetchAndSaveAllDanishMovies() throws Exception {
        List<MovieDTO> movies = getDanishMovies();
        for (MovieDTO dto : movies) {
            Movie movie = toEntity(dto);
            System.out.println("Title length: " + (movie.getTitle() == null ? 0 : movie.getTitle().length()));
            System.out.println("Overview length: " + (movie.getOverview() == null ? 0 : movie.getOverview().length()));
            System.out.println("Lang length: " + (movie.getOriginalLanguage() == null ? 0 : movie.getOriginalLanguage().length()));
            movieDAO.save(movie);
        }
    }

    public Movie toEntity(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setOverview(dto.getOverview());
        movie.setRating(dto.getVoteAverage());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setOriginalLanguage(dto.getOriginalLanguage());

        if (dto.getGenres() != null) {
            for (GenreDTO genredto : dto.getGenres()) {
                Genre genre = new Genre();
                genre.setName(genredto.getName());
                movie.addGenre(genre);
            }
        }

        if (dto.getCast() != null) {
            for (ActorDTO actordto : dto.getCast()) {
                Actor actor = new Actor();
                actor.setName(actordto.getName());
                movie.addActor(actor);
            }
        }

        if (dto.getDirector() != null) {
            Director director = new Director();
            director.setName(dto.getDirector().getName());
            movie.setDirector(director);
        }

        return movie;
    }

    private String fetch(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("GET " + url);
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("TMDB fejl " + response.statusCode() + ": " + response.body());
        }

        return response.body();
    }
}*/