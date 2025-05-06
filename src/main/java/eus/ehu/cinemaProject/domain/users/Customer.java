package eus.ehu.cinemaProject.domain.users;

import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customers")
@DiscriminatorValue("Customer")
public class Customer extends User {
    private int points;
    private double totalBalance;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "customer")
    private List<PurchaseReceipt> purchaseHistory;

    // Default constructor required by JPA
    protected Customer() {}

    // Private constructor used by the builder
    private Customer(CustomerBuilder builder) {
        super(builder);
        this.points = builder.points;
        this.totalBalance = builder.totalBalance;
    }

    // Getters and setters
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public double getTotalBalance() { return totalBalance; }
    public void setTotalBalance(double totalBalance) { this.totalBalance = totalBalance; }

    // Concrete builder for Customer that extends UserBuilder
    public static class CustomerBuilder extends UserBuilder<CustomerBuilder> {
        private int points = 0;
        private double totalBalance = 0.0;

        public CustomerBuilder(String email, String password) {
            super(email, password);
        }

        @Override
        protected CustomerBuilder self() {
            return this;
        }

        public CustomerBuilder points(int points) {
            this.points = points;
            return this;
        }

        public CustomerBuilder totalBalance(double totalBalance) {
            this.totalBalance = totalBalance;
            return this;
        }

        @Override
        public Customer build() {
            return new Customer(this);
        }
    }

    public void addReceipt(PurchaseReceipt purchaseReceipt){purchaseHistory.add(purchaseReceipt);}
}
