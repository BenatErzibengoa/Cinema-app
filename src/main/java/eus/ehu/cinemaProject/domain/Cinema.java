package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Admin;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cinema")
public class Cinema {
    @Id
    private String name;
    private String address;
    private int phoneNumber;
    private LocalTime openingTime;
    private LocalTime closingTime;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<ScreeningRoom> screeningRooms;
    @OneToOne
    private Admin admin;

    public Cinema(String name, String address, int phoneNumber, LocalTime openingTime, LocalTime closingTime){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Cinema(){}

    public LocalTime getOpeningTime(){
        return openingTime;
    }
    public LocalTime getClosingTime(){
        return closingTime;
    }

}