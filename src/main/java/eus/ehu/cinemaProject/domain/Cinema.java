package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Admin;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;

public class Cinema {
    private String name;
    private String address;
    private int phoneNumber;
    private Cinema instance;
    @OneToMany
    private List<ScreeningRoom> screeningRooms;
    @OneToOne
    private Admin admin;

    private Cinema(String name, String address, int phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public Cinema getInstance(String name, String address, int phoneNumber) {
        if (instance == null) {
            instance = new Cinema(name, address, phoneNumber);
        }
        return instance;
    }
    public Cinema getInstance() {
        return instance;
    }
}
