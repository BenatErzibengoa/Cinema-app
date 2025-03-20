package eus.ehu.cinemaproject.domain.users;

import eus.ehu.cinemaproject.domain.PurchaseReceipt;

import java.util.List;

public class Customer extends User {
    private int points;
    private double totalBalance;
    private List<PurchaseReceipt> purchaseHistory;
}
