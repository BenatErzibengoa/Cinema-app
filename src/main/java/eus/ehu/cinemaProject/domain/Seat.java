package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;
import javafx.scene.image.Image;

@Entity
@Table(name = "seats")

public class Seat {
    @Id
    private String seatId;

    @ManyToOne
    private ScreeningRoom screeningRoom;

    private double price;
    private SeatType type;

    private void setPrice(){
        switch (type){
            case NORMAL -> price = 6.5;
            case COMFORTABLE -> price = 7.95;
            case PREMIUM -> price = 9.5;
        }
    }
    public Image getImage(){
        Image im = null;
        switch (type){
            case NORMAL -> im = new Image("plasticSeat.jpeg");
            case COMFORTABLE -> im = new Image("redSeat.png");
            case PREMIUM -> im = new Image("premiumSeat.png");
        }
        return im;
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


    public String getSeatId() { return seatId; }

    public SeatType getType() { return type; }

    @Override
    public String toString(){
        return this.seatId;
    }
}
