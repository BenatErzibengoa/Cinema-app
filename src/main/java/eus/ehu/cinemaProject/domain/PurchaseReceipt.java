package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Customer;

import java.util.Date;
public class PurchaseReceipt {
    private Date orderDate;
    private Customer customer;
    private double totalAmount;
    private OrderStatus status = OrderStatus.RESERVED;
    //instead of having a Review attribute. Use a boolean to check if the customer can review the film and store the name of the film
    private boolean canReview = false;
    private String filmName;

    public PurchaseReceipt(Date orderDate, Customer customer, double totalAmount) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.totalAmount = totalAmount;
    }

}
