package eus.ehu.cinemaProject.domain.users;

import eus.ehu.cinemaProject.domain.PurchaseReceipt;

import java.util.List;

public class Customer extends User {
    private int points;
    private double totalBalance;
    private List<PurchaseReceipt> purchaseHistory;
}
