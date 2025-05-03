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

    // Default constructor required by JPA
    protected Admin() {}

    // Private constructor used by the builder
    private Admin(Admin.AdminBuilder builder) {
        super(builder);;
    }

    public static class AdminBuilder extends WorkerBuilder {
        public AdminBuilder(String email, String password, int salary) {
            super(email, password, salary);
        }

        @Override
        protected Admin.AdminBuilder self() {
            return this;
        }

        @Override
        public Admin build() {
            return new Admin(this);
        }
    }

}
