package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")

public class Seat {
    @Id
    private String seatId;
    private double price;
    @ManyToOne
    private ShowTime showTime;

    @ManyToOne
    private ScreeningRoom screeningRoom;
    private SeatType type;

    private void setPrice(){
        switch (type){
            case NORMAL -> price = 6.5;
            case COMFORTABLE -> price = 7.95;
            case PREMIUM -> price = 9.5;
        }
    }


    public Seat(ScreeningRoom screeningRoom, String seatId, SeatType type){
        this.screeningRoom = screeningRoom;
        this.seatId = seatId;
        this.type = type;
        setPrice();

    }
    public Seat(){}

}
