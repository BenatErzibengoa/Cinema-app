package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Admin;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cinema")
public class Cinema {
    @Id
    private String name;
    private String address;
    private int phoneNumber;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<ScreeningRoom> screeningRooms;
    @OneToOne
    private Admin admin;

    public Cinema(String name, String address, int phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Cinema(){}

}