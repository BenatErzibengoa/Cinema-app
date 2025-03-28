package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "screeningrooms")

public class ScreeningRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    private List<ShowTime> screening;

    @ManyToOne
    private Cinema cinema;
    private Date openingTime;
    private Date closingTime;
}
