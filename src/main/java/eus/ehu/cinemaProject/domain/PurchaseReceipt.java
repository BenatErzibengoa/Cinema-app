package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Customer;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchasereceipts")
public class PurchaseReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date orderDate;
    private double totalAmount;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Film film;
    private OrderStatus status = OrderStatus.RESERVED;
    @ManyToOne
    private Review review;

    public PurchaseReceipt(Date orderDate, double totalAmount, Customer customer, Film film) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.film = film;
        this.status = OrderStatus.RESERVED;
    }

    public PurchaseReceipt(){}

    public PurchaseReceipt(Date date, int i, double v, Customer customer1, Film film1) {
        this(date, v, customer1, film1);
    }

    /*
    TODO: Implement FoodOrder and Quantity
     */

}
