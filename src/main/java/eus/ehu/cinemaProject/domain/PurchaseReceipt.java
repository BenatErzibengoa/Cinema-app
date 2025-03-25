package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Customer;
import jakarta.persistence.ManyToOne;

import java.util.Date;

public class PurchaseReceipt {
    private Date orderDate;
    private double totalAmount;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Film film;
    private OrderStatus status = OrderStatus.RESERVED;
    @ManyToOne
    private Review review;

    /*
    TODO: Implement FoodOrder and Quantity
     */

}
