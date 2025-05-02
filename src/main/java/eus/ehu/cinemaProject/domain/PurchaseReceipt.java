package eus.ehu.cinemaProject.domain;

import eus.ehu.cinemaProject.domain.users.Customer;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private ShowTime showTime;
    private OrderStatus status = OrderStatus.RESERVED;
    @ManyToOne
    private Review review;
    @ManyToMany
    private List<Seat> bookedSeats = new ArrayList<>();

    public PurchaseReceipt(Date orderDate, Customer customer, ShowTime showTime, List<Seat> bookedSeats) {
        this.orderDate = orderDate;
        this.customer = customer;
        this.showTime = showTime;
        this.status = OrderStatus.RESERVED;
        this.bookedSeats = bookedSeats;
        this.totalAmount = getSeatAmount() + getFoodAmount();
        this.showTime.bookSeats(bookedSeats);
    }

    public PurchaseReceipt(){}

    public double getSeatAmount(){
        double amount = 0;
        for(Seat bookedSeat : bookedSeats){
            amount += bookedSeat.getPrice();
        }
        return amount;
    }

    public long getId(){return id;}
    public Date getOrderDate(){return orderDate;}
    public ShowTime getShowTime(){return showTime;}
    public List<Seat> getBookedSeats(){return bookedSeats;}
    public double getTotalAmount(){return totalAmount;}


    /*
    TODO: Implement FoodOrder and Quantity
     */

    public double getFoodAmount(){
        return 0;
    }



}
