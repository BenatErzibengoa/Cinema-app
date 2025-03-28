package eus.ehu.cinemaProject.domain.users;

import eus.ehu.cinemaProject.domain.Cinema;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends Worker {

    public Admin(String email, String password, String name, String surname, int salary) {
        super(email, password, name, surname, salary);
    }
    public Admin(){}

    @OneToOne (mappedBy = "admin")
    private Cinema cinema;

}
