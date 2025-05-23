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
    @Column(length = 1000)
    private String description;
    private List<Genre> genre;
    @OneToMany (mappedBy = "reviewedFilm")
    private List<Review> reviews;
    private String imagePath;  // movie cover - Théo

    public Film(String title, String director, LocalTime duration, String description, List<Genre> genre, String imagePath) {
        this.title = title;
        this.director = director;
        this.duration = duration;
        this.description = description;
        this.genre = genre;
        this.imagePath = imagePath;
    }
    public Film(){}

    public String getTitle(){return this.title;}
    public LocalTime getDuration(){return duration;}
    public String getImagePath() {return imagePath;} //Théo


    public void setImagePath(String imagePath) { //Théo
        this.imagePath = imagePath;
    }
    public String getDescription(){return description;}
    public List<Genre> getGenre(){return genre;}

    @Override
    public String toString(){
        return String.format("Title: %s \nDirector: %s\n Duration: %s\n Description: %s", title, director, duration, description);
    }
}
