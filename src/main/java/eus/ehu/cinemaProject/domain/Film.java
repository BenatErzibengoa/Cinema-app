package eus.ehu.cinemaProject.domain;

import jakarta.persistence.OneToMany;

import java.util.List;

public class Film {
    private String title;
    private String director;
    private int duration;
    private String description;
    private List<Genre> genre;
    @OneToMany
    private List<Review> reviews;
}
