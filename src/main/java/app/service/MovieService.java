package app.service;

import app.config.HibernateConfig;
import app.daos.ActorDAO;
import app.daos.DirectorDAO;
import app.daos.GenreDAO;
import app.daos.MovieDAO;
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
            String json = fetch(BASE_URL + "/discover/movie?api_key=" + apiKey
                    + "&with_original_language=da&page=" + page);
            MovieDTO.PageResult response = objectMapper.readValue(json, MovieDTO.PageResult.class);
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
            movieDAO.save(movie);
        }
    }

    public Movie toEntity(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setOverview(dto.getOverview());
        movie.setVoteAverage(dto.getVoteAverage());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setOriginalLanguage(dto.getOriginalLanguage());

        if (dto.getGenres() != null) {
            List<Genre> genres = dto.getGenres().stream()
                    .map(g -> {
                        Genre genre = new Genre();
                        genre.setName(g.getName());
                        return genre;
                    }).toList();
            movie.setGenres(genres);
        }

        if (dto.getCast() != null) {
            List<Actor> actors = dto.getCast().stream()
                    .map(a -> {
                        Actor actor = new Actor();
                        actor.setName(a.getName());
                        return actor;
                    }).toList();
            movie.setActors(actors);
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
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}