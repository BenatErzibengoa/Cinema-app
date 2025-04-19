package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Customer;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Film reviewedFilm;
    private int rating;
    private String textReview;
    @ManyToOne
    private Customer author;

    public Review(Film reviewedFilm, int rating, String textReview, Customer author) {
        this.reviewedFilm = reviewedFilm;
        this.rating = rating;
        this.textReview = textReview;
        this.author = author;
    }
    public Review(){}

}
