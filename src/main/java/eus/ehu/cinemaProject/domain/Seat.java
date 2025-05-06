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
            case NORMAL -> name ="seat.png";
            case COMFORTABLE -> name = "premium seat.png";
            case PREMIUM -> name = "premium seat.png";
            case OCCUPIED -> name = "occupiedSeat.png";
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
    public void setType(SeatType type) {
        this.type = type;
    }

    @Override
    public String toString(){
        String[] myArray = seatId.split("\\.");
        return String.format("Room: %s, Row: %s, Seat: %s", myArray[0], myArray[1], myArray[2]);    }

    public boolean isReserved() {
        return type == SeatType.OCCUPIED;
    }
}
