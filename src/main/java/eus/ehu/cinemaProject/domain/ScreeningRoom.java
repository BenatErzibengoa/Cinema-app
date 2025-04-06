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

    @OneToMany (mappedBy = "screeningRoom")
    private List<ShowTime> showTimes;

    @OneToMany(mappedBy = "id.screeningRoom")
    private List<Schedule> schedules;


    @ManyToOne
    private Cinema cinema;

    public List<Seat> getSeats() {
        return seats;
    }


    public ScreeningRoom(Cinema cinema, int roomNumber){
        this.cinema = cinema;
        this.roomNumber = roomNumber;
        //Room.Row.Seat
        for(int i = 1; i <= 10; i++){
            Seat seat = new Seat(this, String.format("%s.%s.%s", roomNumber, 1, i), SeatType.NORMAL);
            seats.add(seat);
        }
        for(int i = 11; i <= 20; i++){
            Seat seat = new Seat(this, String.format("%s.%s.%s", roomNumber, 2, i), SeatType.NORMAL);
            seats.add(seat);
        }
        for(int i = 21; i <= 30; i++){
            Seat seat = new Seat(this, String.format("%s.%s.%s", roomNumber, 3, i), SeatType.COMFORTABLE);
            seats.add(seat);
        }
        for(int i = 31; i <= 40; i++){
            Seat seat = new Seat(this, String.format("%s.%s.%s", roomNumber, 4, i), SeatType.PREMIUM);
            seats.add(seat);
        }
    }

    public ScreeningRoom() {}
    public int getRoomNumber(){return this.roomNumber;}
    public Cinema getCinema(){return this.cinema;}
}
