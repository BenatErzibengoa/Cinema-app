package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Customer;

import java.util.Date;

public class PurchaseReceipt {
    private Date orderDate;
    private double totalAmount;
    private Customer customer;
    private OrderStatus status = OrderStatus.RESERVED;
    private Film film;
    private Review review;

}
