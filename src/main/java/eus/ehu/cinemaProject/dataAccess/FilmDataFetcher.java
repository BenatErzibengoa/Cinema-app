package eus.ehu.cinemaProject.dataAccess;

import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Genre;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FilmDataFetcher {

    private static final String API_KEY = "4f7b23dc0dc0fe3332d6425df703e845"; // Tu API Key de TMDb

    public static Film fetchFilmDataByName(String movieName) {
        try {
            // Paso 1: Hacer una búsqueda por nombre
            String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + movieName + "&language=en-US";
            URL url = new URL(searchUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Leer la respuesta de la búsqueda
            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            // Paso 2: Obtener el primer resultado de la búsqueda
            JsonArray results = jsonResponse.getAsJsonArray("results");
            if (results.size() == 0) {
                return null;  // Si no se encuentra ninguna película
            }

            JsonObject firstResult = results.get(0).getAsJsonObject();
            String movieId = firstResult.get("id").getAsString();  // Obtener el ID de la película encontrada

            // Paso 3: Obtener los detalles de la película con el movieId
            return fetchFilmDataById(movieId);  // Llamamos a la función que obtiene los detalles

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Film fetchFilmDataById(String movieId) {
        try {
            //Set API fetch URL
            String urlString = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY + "&language=en-US";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read API response
            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            // Extract relevant info about API
            String title = jsonResponse.get("title").getAsString();
            String director = fetchDirector(movieId);  // Director is processed separately
            String description = jsonResponse.get("overview").getAsString();
            String imagePath = "https://image.tmdb.org/t/p/w500" + jsonResponse.get("poster_path").getAsString();

            // Get duration and parse to LocalTime
            int durationInMinutes = jsonResponse.get("runtime").getAsInt();
            java.time.LocalTime duration = java.time.LocalTime.of(durationInMinutes / 60, durationInMinutes % 60);

            // Obtain genre (match genre with enum)
            List<Genre> genres = new ArrayList<>();
            JsonArray genresArray = jsonResponse.getAsJsonArray("genres");
            for (int i = 0; i < genresArray.size(); i++) {
                String genreName = genresArray.get(i).getAsJsonObject().get("name").getAsString();
                try {
                    Genre genre = Genre.valueOf(genreName.toUpperCase());
                    genres.add(genre);
                } catch (IllegalArgumentException e) {
                    // If it doesn't match with enum, we skip it
                    System.out.println("Género no reconocido: " + genreName);
                }
            }
            // Return film object
            return new Film(title, director, duration, description, genres, imagePath);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener el director de la película
    private static String fetchDirector(String movieId) {
        try {
            String urlString = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY + "&language=en-US";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Leer la respuesta
            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);

            // Extraer el nombre del director
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
        return "Unknown";  // Si no se encuentra el director
    }
}
