package eus.ehu.cinemaProject.domain.users;

import eus.ehu.cinemaProject.domain.Cinema;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends Worker {
    @OneToOne (mappedBy = "admin")
    private Cinema cinema;

}
