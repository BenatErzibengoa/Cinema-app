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

    @OneToMany(mappedBy = "screeningRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<Seat>();

    @OneToMany(mappedBy = "id.screeningRoom")
    private List<Schedule> schedules;

    @OneToMany
    private List<ShowTime> screening;

    @ManyToOne
    private Cinema cinema;

    private int MAX_ROWS = 4;
    private int MAX_SEATS_PER_ROW = 10;

    public List<Seat> getSeats() {
        return seats;
    }

    //TODO: Refine the row and seat numbers to vary depending on the characteristics of the room. Add possibility of changing dimensions
    public ScreeningRoom(Cinema cinema, int roomNumber){
        this.cinema = cinema;
        this.roomNumber = roomNumber;
        //Room.Row.Seat
        for(int i=1; i<=MAX_ROWS; i++)
            for (int j=1; j<=MAX_SEATS_PER_ROW; j++)
                seats.add( new Seat(this, String.format("%s.%s.%s", roomNumber, i, j), SeatType.values()[(i-1)%3]));

    }

    public ScreeningRoom() {}
    public int getRoomNumber(){return this.roomNumber;}
    public Cinema getCinema(){return this.cinema;}

    public int getMAX_ROWS() { return MAX_ROWS; }
    public void setMAX_ROWS(int MAX_ROWS) { this.MAX_ROWS = MAX_ROWS; }
    public int getMAX_SEATS_PER_ROW() { return MAX_SEATS_PER_ROW; }
    public void setMAX_SEATS_PER_ROW(int MAX_SEATS_PER_ROW) { this.MAX_SEATS_PER_ROW = MAX_SEATS_PER_ROW; }
}
