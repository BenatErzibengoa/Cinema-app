package eus.ehu.cinemaProject.domain;

import java.time.LocalDate;
import java.time.LocalTime;
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

    private LocalDate screeningDate;
    private LocalTime screeningTime;

    @OneToMany
    private Set<Seat> bookedSeats;  //It is a set because we will not fetch any specific seat from this set, it will only be used for .contains(seat) by a controller

    @ManyToOne
    private Schedule schedule;

    @ManyToOne
    private Film film;

    public ShowTime(ScreeningRoom screeningRoom, Schedule schedule, LocalTime screeningTime, Film film){
        this.screeningRoom = screeningRoom;
        this.schedule = schedule;
        this.screeningDate = schedule.getDate(); // We get Date from the Schedule associated
        this.screeningTime = screeningTime;      // We get Time from parameters
        this.film = film;
        this.bookedSeats = new HashSet<>();
    }

    public void bookSeats(List<Seat> seatsToBook){
        bookedSeats.addAll(seatsToBook);
    }

    public ShowTime(){}

    public long getId(){return this.id;}
    public LocalTime getScreeningTime(){return this.screeningTime;}
    public Film getFilm(){return film;}

}
