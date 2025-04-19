package eus.ehu.cinemaProject.domain;

import jakarta.persistence.*;
import javafx.scene.image.Image;

import java.io.InputStream;

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
        String name = "";
        switch (type){
            case NORMAL -> name ="plasticSeat.jpeg";
            case COMFORTABLE -> name = "redSeat.png";
            case PREMIUM -> name = "premiumSeat.png";
        }
        InputStream stream = getClass().getResourceAsStream("/eus/ehu/cinemaProject/ui/pictures/" + name);
        if (stream == null) {
            throw new IllegalArgumentException("Seat image not found");
        }
        return new Image(stream);
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
