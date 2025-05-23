package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.configuration.PasswordHasher;
import eus.ehu.cinemaProject.domain.*;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;

import eus.ehu.cinemaProject.configuration.Config;
import eus.ehu.cinemaProject.dataAccess.DataAccess;
import eus.ehu.cinemaProject.domain.users.Worker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Implements the business logic as a web service.
 */
public class BlFacadeImplementation implements BlFacade {

    DataAccess dbManager;
    Config config = Config.getInstance();

    private static BlFacadeImplementation bl = new BlFacadeImplementation();

    public static BlFacadeImplementation getInstance() {
        return bl;
    }

    private BlFacadeImplementation() {
        System.out.println("Creating BlFacadeImplementation instance");
        boolean initialize = config.getDataBaseOpenMode().equals("initialize");
        dbManager = new DataAccess();
        if (initialize)
            dbManager.initializeDB();
    }

    public User login(String email, String password) {
        User user = getUserByEmail(email);
        if (user == null || !PasswordHasher.checkPassword(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    public User getUserByEmail(String email) {
        return dbManager.getUserByEmail(email);
    }

    public void signUp(String email, String password, String name, String surname) {
        dbManager.signUp(email, password, name, surname);
    }

    public List<ScreeningRoom> getScreeningRooms() {
        return dbManager.getScreeningRooms();
    }

    public List<ShowTime> getShowTimesByDate(LocalDate date) {
        return dbManager.getShowTimesByDate(date);
    }

    public List<ShowTime> getShowTimesByDateAndFilm(LocalDate date, Film film) {
        return dbManager.getShowTimesByDateAndFilm(date, film);
    }

    public void createPurchaseReceipt(Customer customer, ShowTime showTime, List<Seat> seats) {
        dbManager.createPurchaseReceipt(customer, showTime, seats);
    }

    public List<PurchaseReceipt> getPurchaseReceiptsByUser(Customer customer) {
        return dbManager.getPurchaseReceiptsByUser(customer);
    }

    public void storeReview(Film reviewedFilm, int rating, String textReview, Customer author) {
        dbManager.storeReview(reviewedFilm, rating, textReview, author);
    }

    public double getAverageRating(Film film) {
        Double average = dbManager.getAverageRating(film);
        if (average == null) {
            return 0;
        }
        return average;
    }

    public boolean hasFilmBeenReviewed(Film film, Customer customer) {
        return dbManager.getReviewByFilmAndUser(film, customer) != null;
    }

    public List<Review> getReviewsByFilm(Film film) {
        return dbManager.getReviewsByFilm(film);
    }

    public List<Worker> getAllWorkers() {
        return dbManager.getAllWorkers();
    }

    public void deleteWorker(Worker worker) {
        dbManager.deleteWorker(worker);
    }

    public User signUpWorker(String email, String password, String name, String surname, int salary) {
        return dbManager.signUpWorker(email, password, name, surname, salary);
    }

    public List<Film> getAllFilms() {
        return dbManager.getAllFilms();
    }

    public Schedule getScheduleByRoomAndDate(LocalDate date, ScreeningRoom screeningRoom) {
        return dbManager.getScheduleByRoomAndDate(date, screeningRoom);
    }
    public void setOrderStatus(PurchaseReceipt receipt, OrderStatus orderStatus) {
        dbManager.setOrderStatus(receipt, orderStatus);
    }

    public void saveShowTime(ShowTime showTime){dbManager.saveShowTime(showTime);}

    public void addFilm(Film film){ dbManager.addFilm(film);}

    public Film getFilmbyName(String name) {
        return dbManager.getFilmbyName(name);
    }

    public List<ShowTime>getAllShowtimes(){return dbManager.getAllShowtimes();}
    public List<PurchaseReceipt> getPendingCancellationPurchaseReceipts() {
        return dbManager.getPendingCancellationPurchaseReceipts();
    }
}



