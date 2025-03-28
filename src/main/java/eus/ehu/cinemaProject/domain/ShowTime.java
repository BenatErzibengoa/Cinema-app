package eus.ehu.cinemaProject.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date screeningTime;
    @OneToMany (mappedBy = "showTime")
    private Set<Seat> seats;

    @ManyToOne
    private Film film;

}
