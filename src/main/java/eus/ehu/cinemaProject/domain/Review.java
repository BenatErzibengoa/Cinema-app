package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Film reviewedFilm;
    private String opinion;
    private int rating;
    private String reviewAuthor;
}
