package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "screeningrooms")

public class ScreeningRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomNumber;
    @ManyToMany
    private List<ShowTime> screenings;
    private Date openingTime;
    private Date closingTime;
}
