package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "screeningrooms")

public class ScreeningRoom {
    @Id
    private int roomNumber;

    @OneToMany
    private List<ShowTime> screening;

    @OneToMany(mappedBy = "screeningRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<Seat>();


    @OneToMany(mappedBy = "id.screeningRoom")
    private List<Schedule> schedules;


    @ManyToOne
    private Cinema cinema;

    public List<Seat> getSeats() {
        return seats;
    }

    //TODO: Refine the row and seat numbers to vary depending on the characteristics of the room. Add possibility of changing dimensions
    public ScreeningRoom(Cinema cinema, int roomNumber){
        this.cinema = cinema;
        this.roomNumber = roomNumber;
        //Room.Row.Seat
        for(int i=1; i<5; i++)
            for (int j=1; j<11; j++)
                seats.add( new Seat(this, String.format("%s.%s.%s", roomNumber, i, j), SeatType.values()[(i-1)%3]));

    }

    public ScreeningRoom() {}
    public int getRoomNumber(){return this.roomNumber;}
    public Cinema getCinema(){return this.cinema;}
}
