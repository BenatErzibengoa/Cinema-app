package eus.ehu.cinemaProject.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "workers")
public class Worker extends User {
    private int Salary;
}
