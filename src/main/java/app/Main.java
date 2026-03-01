package app;

import app.service.MovieService;

public class Main {
    public static void main(String[] args) throws Exception {

        MovieService movieService = new MovieService();
        movieService.fetchAndStoreDanishMoviesLast5Years();

    }
}