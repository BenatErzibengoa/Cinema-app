package eus.ehu.cinemaproject.domain;

public class Seat {
    private ScreeningRoom room;
    private String seatId;
    private double price;
    private SeatType type;

    private void setPrice(){
        switch (type){
            case NORMAL -> price = 6.5;
            case COMFORTABLE -> price = 7.95;
            case PREMIUM -> price = 9.5;
        }
    }

    public Seat(ScreeningRoom room, String seatId, SeatType type){
        this.room = room;
        this.seatId = seatId;
        this.type = type;
        setPrice();
    }
}
