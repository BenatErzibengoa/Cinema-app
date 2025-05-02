package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.domain.*;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.domain.users.Worker;

import java.time.LocalDate;
import java.time.LocalTime;
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
     * @param email the email
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
     * @param date the date
     */
    List<ShowTime> getShowTimesByDate(LocalDate date);

    /**
     * Returns all ShowTimes that matches with the provided date and film
     *
     * @param date the date
     * @param film the film
     */
    List<ShowTime> getShowTimesByDateAndFilm(LocalDate date, Film film);

    /**
     * Creates a PurchaseReceipt for a customer
     *
     * @param customer the date
     * @param showTime the showtime
     * @param seats    the booked seats
     *                 TODO: Food
     */
    void createPurchaseReceipt(Customer customer, ShowTime showTime, List<Seat> seats);

    /**
     * Returns all PurchaseReceipts that matches with the provided customer
     *
     * @param customer the customer
     */
    List<PurchaseReceipt> getPurchaseReceiptsByUser(Customer customer);

    /**
     * Returns the average rating among all the reviews of a film
     *
     * @param film the film
     */
    double getAverageRating(Film film);

    /**
     * Returns whether a film has been reviewed by a customer or not
     *
     * @param film the film
     * @param customer the customer
     */
    boolean hasFilmBeenReviewed(Film film, Customer customer);

    /**
     * Gets all reviews of a film
     *
     * @param film the film
     */
    List<Review> getReviewsByFilm(Film film);

    /**
     * Returns all workers in the database
     *
     * @return List of workers, null if no workers are found
     */
    List<Worker>getAllWorkers();

    /**
     * Deletes a worker from the database
     *
     * @param worker the worker to be deleted
     */
    void deleteWorker(Worker worker);

    /**
     * Registers a worker with the given credentials and personal data
     *
     * @param email    the email
     * @param password the password
     * @param name     the name
     * @param surname  the surname
     * @param salary the salary
     */
    User signUpWorker(String email, String password, String name, String surname, int salary);

    /**
     * Returns all films in the database
     *
     * @return List of films, null if no films are found
     */
    List<Film> getAllFilms();

    /**
     * Returns schedule that matches with the provided date and screening room
     * @param date
     * @param screeningRoom
     * @return
     */
    Schedule getScheduleByRoomAndDate(LocalDate date, ScreeningRoom screeningRoom);

    /**
     * Stores a showtime in the database and updates corresponding schedule
     * @param showTime
     */
    void saveShowTime(ShowTime showTime);

    /**
     * Stores a film in the database
     * @param film
     */
    void addFilm(Film film);

    /**
     * Returns a film that matches with the provided name
     * @param name
     * @return Film object
     */
    Film getFilmbyName(String name);
    /**
     * Returns all showtimes in the database
     * @return List of showtimes
     */
    List<ShowTime>getAllShowtimes();


}