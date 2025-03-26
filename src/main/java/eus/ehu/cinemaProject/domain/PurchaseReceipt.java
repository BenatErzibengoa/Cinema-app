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

    /*
    TODO: Implement FoodOrder and Quantity
     */

}
