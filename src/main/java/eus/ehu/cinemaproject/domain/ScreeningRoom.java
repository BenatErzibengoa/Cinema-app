package eus.ehu.cinemaproject.domain;

import java.util.Date;
import java.util.List;

public class ScreeningRoom {
    private int roomNumber;
    private List<Seat> seats;
    private List<ShowTime> screenings;
    private Date openingTime;
    private Date closingTime;
}
