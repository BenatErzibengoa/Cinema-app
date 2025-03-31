package eus.ehu.cinemaProject.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "showtimes")

public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private ScreeningRoom screeningRoom;

    private Date screeningTime;
    @OneToMany
    private Set<Seat> bookedSeats;

    @ManyToOne
    private Film film;

    public ShowTime(ScreeningRoom screeningRoom, Date screeningTime, Film film){
        this.screeningRoom = screeningRoom;
        this.screeningTime = screeningTime;
        this.film = film;
        this.bookedSeats = new HashSet<>();
    }

    public void bookSeats(List<Seat> seatsToBook){
        bookedSeats.addAll(seatsToBook);
    }

    public ShowTime(){}

}
