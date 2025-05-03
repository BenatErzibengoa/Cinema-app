package eus.ehu.cinemaProject.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "workers")
public class Worker extends User {
    private int salary;

    // Default constructor required by JPA
    protected Worker() {}

    // Private constructor used by the builder
    protected Worker(Worker.WorkerBuilder builder) {
        super(builder);
        this.salary = builder.salary;
    }
    // Getters and setters
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }

    public static class WorkerBuilder extends UserBuilder<Worker.WorkerBuilder> {
        private int salary = 0;
        public WorkerBuilder(String email, String password, int salary) {
            super(email, password);
            this.salary = salary;
        }

        @Override
        protected Worker.WorkerBuilder self() {
            return this;
        }

        @Override
        public Worker build() {
            return new Worker(this);
        }
    }

}
