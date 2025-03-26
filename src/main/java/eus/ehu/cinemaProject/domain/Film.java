package eus.ehu.cinemaProject.domain;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String director;
    private int duration;
    private String description;
    private List<Genre> genre;
    @OneToMany (mappedBy = "reviewedFilm")
    private List<Review> reviews;
}
