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

    private LocalDate screeningDate;
    private LocalTime screeningTime;

    @ManyToMany
    private Set<Seat> bookedSeats;  //It is a set because we will not fetch any specific seat from this set, it will only be used for .contains(seat) by a controller

    @ManyToOne
    private Schedule schedule;

    @ManyToOne
    private Film film;

    public ShowTime(Schedule schedule, LocalTime screeningTime, Film film){
        this.schedule = schedule;
        this.screeningDate = schedule.getDate(); // We get Date from the Schedule associated
        this.screeningTime = screeningTime;      // We get Time from parameters
        this.film = film;
        this.bookedSeats = new HashSet<>();
    }
    public ShowTime(){}

    public void bookSeats(List<Seat> seatsToBook){
        bookedSeats.addAll(seatsToBook);
    }

    public void setSchedule(Schedule schedule){this.schedule=schedule;}
    public Set<Seat> getBookedSeats(){return bookedSeats;}
    public long getId(){return this.id;}
    public LocalTime getScreeningTime(){return this.screeningTime;}
    public Film getFilm(){return film;}
    public Schedule getSchedule(){return schedule;}

    @Override
    public String toString() {
        return String.format("Room Number: %s, Date: %s, Time: %s, Film: %s", schedule.getScreeningRoom().getRoomNumber(), screeningDate, screeningTime, film.getTitle());
    }

    public String toString2() {
        return String.format(
                "Room Number: %s\nDate: %s\nTime: %s",
                schedule.getScreeningRoom().getRoomNumber(),
                screeningDate,
                screeningTime
        );
    }


}
