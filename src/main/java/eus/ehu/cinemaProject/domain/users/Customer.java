package eus.ehu.cinemaProject.domain.users;

import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
//@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    private int points;
    private double totalBalance;

    //@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<PurchaseReceipt> purchaseHistory;
}
