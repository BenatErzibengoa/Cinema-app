package eus.ehu.cinemaProject.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "workers")
public class Worker extends User {
    public Worker(String email, String password, String name, String surname, int salary) {
        super(email, password, name, surname);
        Salary = salary;
    }
    public Worker(){}
    private int Salary;
}
