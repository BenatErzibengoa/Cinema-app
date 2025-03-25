package eus.ehu.cinemaProject.domain;

import java.util.List;

public class Cinema {
    private String name;
    private String address;
    private int phoneNumber;
    private Cinema instance;
    private List<ScreeningRoom> screeningRooms;

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
