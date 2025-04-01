package eus.ehu.cinemaProject.domain;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String director;
    private LocalTime duration;
    private String description;
    private List<Genre> genre;
    @OneToMany (mappedBy = "reviewedFilm")
    private List<Review> reviews;

    public Film(String title, String director, LocalTime duration, String description, List<Genre> genre) {
        this.title = title;
        this.director = director;
        this.duration = duration;
        this.description = description;
        this.genre = genre;
    }
    public Film(){}

    public LocalTime getDuration(){return duration;}
}
