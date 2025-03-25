package eus.ehu.cinemaProject.domain;

import jakarta.persistence.ManyToMany;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class ScreeningRoom {
    private int roomNumber;
    @ManyToMany
    private List<ShowTime> screenings;
    private Date openingTime;
    private Date closingTime;
}
