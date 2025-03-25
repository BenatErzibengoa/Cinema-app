package eus.ehu.cinemaProject.domain;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


public class ShowTime {
    private Date screeningTime;
    @ManyToMany(mappedBy = "screenings")
    private ScreeningRoom screeningRoom;
    @OneToMany
    private Set<Seat> seats;
    @ManyToOne
    private Film film;

}
