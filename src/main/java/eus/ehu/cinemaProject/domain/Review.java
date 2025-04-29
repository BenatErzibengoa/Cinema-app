package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Customer;
import jakarta.persistence.*;

import java.time.LocalDate;

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
    private LocalDate date;

    public Review(Film reviewedFilm, int rating, String textReview, Customer author) {
        this.reviewedFilm = reviewedFilm;
        this.rating = rating;
        this.textReview = textReview;
        this.author = author;
        this.date = LocalDate.now();

    }
    public Review(){}
    public Customer getAuthor(){return author;}
    public int getRating(){return rating;}
    public String getTextReview(){return textReview;}
    public LocalDate getDate(){return date;}
}
