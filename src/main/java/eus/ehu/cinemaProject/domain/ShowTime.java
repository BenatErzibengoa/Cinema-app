package eus.ehu.cinemaProject.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")

public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date screeningTime;
    @OneToMany
    private Set<Seat> bookedSeats;
    @ManyToOne
    private ScreeningRoom screeningRoom;
    @ManyToOne
    private Film film;

    public ShowTime(Date date, ScreeningRoom screeningRoom, Film film){
        this.screeningTime = date;
        this.screeningRoom = screeningRoom;
        this.film = film;
        this.bookedSeats = new HashSet<>();
    }

    public ShowTime(){}

    public void bookSeats(List<Seat> seats){
        for(Seat seat: seats){
            bookedSeats.add(seat);
        }
    }


}
