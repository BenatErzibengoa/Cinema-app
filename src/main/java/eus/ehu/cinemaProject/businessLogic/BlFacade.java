package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.domain.ShowTime;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Interface that specifies the business logic.
 */
public interface BlFacade {

    // ===== Define the public interface of the BL

    /**
     * Attempts to log in a user with the given credentials
     *
     * @param email    the email
     * @param password the password
     * @return User if login successful, null otherwise
     */
    User login(String email, String password);

    /**
     * Returns the user related to the given email
     *
     * @param email     the email
     */
    User getUserByEmail(String email);

    /**
     * Registers a customer with the given credentials and personal data
     *
     * @param email    the email
     * @param password the password
     * @param name     the name
     * @param surname  the surname
     */
    void signUp(String email, String password, String name, String surname);

    /**
     * Returns all ShowTimes that matches with the provided date
     *
     * @param date    the date
     */
    List<ShowTime> getShowTimesByDate(LocalDate date);

    /**
     * Returns all ShowTimes that matches with the provided date and film
     *
     * @param date    the date
     * @param film    the film
     */
    List<ShowTime> getShowTimesByDateAndFilm(LocalDate date, Film film);

    /**
     * Creates a PurchaseReceipt for a customer
     *
     * @param customer    the date
     * @param showTime    the showtime
     * @param seats       the booked seats
     * TODO: Food
     */
    void createPurchaseReceipt(Customer customer, ShowTime showTime, List<Seat> seats);





}