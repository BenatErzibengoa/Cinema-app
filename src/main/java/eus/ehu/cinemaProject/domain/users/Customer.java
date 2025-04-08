package eus.ehu.cinemaProject.domain.users;

import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@DiscriminatorValue("Customer")
public class Customer extends User {
    private int points;
    private double totalBalance;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "customer")
    private List<PurchaseReceipt> purchaseHistory;

    public Customer(String email, String password, String name, String surname) {
        super(email, password, name, surname);
        this.points = 0;
        this.totalBalance = 0;
        purchaseHistory = new ArrayList<>();
    }

    public Customer(){}

    public void addReceipt(PurchaseReceipt purchaseReceipt){purchaseHistory.add(purchaseReceipt);}
}
