package eus.ehu.cinemaProject.dataAccess;

import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Genre;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FilmDataFetcher {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("TMDB_API_KEY");

    public static Film fetchFilmDataByName(String movieName) {
        try {
            // Encode the movie name
            String encodedMovieName = URLEncoder.encode(movieName, StandardCharsets.UTF_8.toString());

            // Step 1: Search by movie name
            String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + encodedMovieName + "&language=en-US";
            URL url = new URL(searchUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the search API response
            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);
            JsonArray results = jsonResponse.getAsJsonArray("results");

            if (results.size() == 0) {
                return null;  // If no movie is found
            }

            // Get the ID of the first movie found
            JsonObject firstResult = results.get(0).getAsJsonObject();
            String movieId = firstResult.get("id").getAsString();

            // Step 2: Get the movie details using the movieId
            String urlString = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY + "&language=en-US";
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the movie details API response
            reader = new InputStreamReader(conn.getInputStream());
            jsonResponse = gson.fromJson(reader, JsonObject.class);

            // Extract movie details
            String title = jsonResponse.get("title").getAsString();
            String director = fetchDirector(movieId);  // Director is processed separately
            String description = jsonResponse.get("overview").getAsString();
            String imagePath = "https://image.tmdb.org/t/p/w500" + jsonResponse.get("poster_path").getAsString();

            // Get the duration and parse it to LocalTime
            int durationInMinutes = jsonResponse.get("runtime").getAsInt();
            java.time.LocalTime duration = java.time.LocalTime.of(durationInMinutes / 60, durationInMinutes % 60);

            // Obtain genres (match genres with enum)
            List<Genre> genres = new ArrayList<>();
            JsonArray genresArray = jsonResponse.getAsJsonArray("genres");
            for (int i = 0; i < genresArray.size(); i++) {
                String genreName = genresArray.get(i).getAsJsonObject().get("name").getAsString();
                try {
                    Genre genre = Genre.valueOf(genreName.toUpperCase());
                    genres.add(genre);
                } catch (IllegalArgumentException e) {
                    // If it doesn't match the enum, skip it
                    System.out.println("Unrecognized genre: " + genreName);
                }
            }

            // Return the film object
            return new Film(title, director, duration, description, genres, imagePath);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static String fetchDirector(String movieId) {
        try {
            String urlString = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY + "&language=en-US";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            JsonArray crewArray = jsonResponse.getAsJsonArray("crew");
            for (int i = 0; i < crewArray.size(); i++) {
                JsonObject crewMember = crewArray.get(i).getAsJsonObject();
                String job = crewMember.get("job").getAsString();
                if (job.equalsIgnoreCase("Director")) {
                    return crewMember.get("name").getAsString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";  // if not found
    }
}
