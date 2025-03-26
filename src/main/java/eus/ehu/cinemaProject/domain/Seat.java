package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")

public class Seat {
    @Id
    private String seatId;
    private double price;
    @OneToMany (mappedBy = "seats")
    private ShowTime showTime;
    private SeatType type;

    private void setPrice(){
        switch (type){
            case NORMAL -> price = 6.5;
            case COMFORTABLE -> price = 7.95;
            case PREMIUM -> price = 9.5;
        }
    }

    public Seat(ShowTime showTime, String seatId, SeatType type){
        this.showTime = showTime;
        this.seatId = seatId;
        this.type = type;
        setPrice();
    }
}
