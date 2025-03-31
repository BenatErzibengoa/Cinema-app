package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")

public class Seat {
    @Id
    @Column(nullable = false, unique = true)
    private String seatId;
    private double price;

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

    public double getPrice() {
        return price;
    }
}
