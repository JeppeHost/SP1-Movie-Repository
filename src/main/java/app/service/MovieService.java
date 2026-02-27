package app.service;

import app.config.HibernateConfig;
import app.daos.ActorDAO;
import app.daos.DirectorDAO;
import app.daos.GenreDAO;
import app.daos.MovieDAO;
import app.dtos.CreditsDTO;
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
import java.time.LocalDate;
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

    public List<MovieDTO> getDanishMovies() throws Exception {
        List<MovieDTO> all = new ArrayList<>();
        int page = 1;
        String fiveYearsAgo = LocalDate.now().minusYears(5).toString();

        while (true) {
            String json = fetch(BASE_URL + "/discover/movie?api_key=" + apiKey
                    + "&with_original_language=da"
                    + "&primary_release_date.gte=" + fiveYearsAgo
                    + "&page=" + page);
            MovieDTO.PageResult response = objectMapper.readValue(json, MovieDTO.PageResult.class);
            if (response.getResults() == null) break;
            all.addAll(response.getResults());
            if (page >= response.getTotalPages()) break;
            page++;
        }
        return all;
    }

    public CreditsDTO getCredits(Long movieId) throws Exception {
        String json = fetch(BASE_URL + "/movie/" + movieId + "/credits?api_key=" + apiKey);
        return objectMapper.readValue(json, CreditsDTO.class);
    }

    public void fetchAndSaveAllDanishMovies() throws Exception {
        List<MovieDTO> movies = getDanishMovies();
        for (MovieDTO dto : movies) {
            CreditsDTO credits = getCredits(dto.getId());
            Movie movie = toEntity(dto, credits);

            if (movie.getDirector() != null && movie.getDirector().getId() == null) {
                directorDAO.save(movie.getDirector());
            }

            for (Actor actor : movie.getActors()) {
                if (actor.getId() == null) {
                    actorDAO.save(actor);
                }
            }

            for (Genre genre : movie.getGenres()) {
                if (genre.getId() == null) {
                    genreDAO.save(genre);
                }
            }

            movieDAO.save(movie);
        }
    }

    public Movie toEntity(MovieDTO dto, CreditsDTO credits) {
        Movie movie = new Movie();
        movie.setId(dto.getId().intValue());
        movie.setTitle(dto.getTitle());
        movie.setOverview(dto.getOverview());
        movie.setRating(dto.getVoteAverage());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setOriginalLanguage(dto.getOriginalLanguage());
        movie.setPopularity(dto.getPopularity());

        if (dto.getGenres() != null) {
            for (GenreDTO genreDTO : dto.getGenres()) {
                Genre genre = genreDAO.findByName(genreDTO.getName());
                if (genre == null) genre = new Genre(genreDTO.getName());
                movie.addGenre(genre);
            }
        }

        if (credits.getCast() != null) {
            for (CreditsDTO.CastMemberDTO castMember : credits.getCast()) {
                Actor actor = actorDAO.findByName(castMember.getName());
                if (actor == null) actor = new Actor(castMember.getName());
                movie.addActor(actor);
            }
        }

        if (credits.getCrew() != null) {
            credits.getCrew().stream()
                    .filter(c -> "Director".equals(c.getJob()))
                    .findFirst()
                    .ifPresent(directorMember -> {
                        Director director = directorDAO.findByName(directorMember.getName());
                        if (director == null) director = new Director(directorMember.getName());
                        movie.setDirector(director);
                    });
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