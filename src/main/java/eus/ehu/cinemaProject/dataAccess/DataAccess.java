package eus.ehu.cinemaProject.dataAccess;

import eus.ehu.cinemaProject.configuration.Config;
import eus.ehu.cinemaProject.configuration.PasswordHasher;
import eus.ehu.cinemaProject.domain.*;
import eus.ehu.cinemaProject.domain.users.Admin;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.domain.users.Worker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import javafx.concurrent.Task;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class DataAccess {

    private static final Logger logger = LogManager.getLogger(DataAccess.class);
    protected EntityManager db;
    protected EntityManagerFactory emf;

    public DataAccess() {
        this.open();
        // Add shutdown hook to close the database when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down database connection...");
            this.close();
        }));
        Configurator.setLevel(logger.getName(), Level.ALL);
    }

    public void open() {
        open(false);
    }


    public void open(boolean initializeMode) {
        Config config = Config.getInstance();
        logger.info("Opening DataAccess instance => isDatabaseLocal: " +
                config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());

        String fileName = config.getDatabaseName();
        if (initializeMode) {
            fileName = fileName + ";drop";
            logger.info("Deleting the DataBase");
        }

        if (config.isDataAccessLocal()) {
            logger.info("Loading Hibernate configuration...");
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            try {
                emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
                // so destroy it manually.
                StandardServiceRegistryBuilder.destroy(registry);
                e.printStackTrace();
            }

            db = emf.createEntityManager();
            logger.info("DataBase opened");
        }
    }

    public void reset() {
        db.getTransaction().begin();
        db.createQuery("DELETE FROM User").executeUpdate();
        db.getTransaction().commit();
    }

    public void initializeDB() {
        this.reset();
        try {
            db.getTransaction().begin();
            generateTestingData();
            db.getTransaction().commit();
            logger.info("The database has been initialized");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public User signUp(String email, String password, String name, String surname){
        User user = new Customer(email, PasswordHasher.hashPassword(password),name,surname);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(user);
        db.getTransaction().commit();
        return user;
    }

    public User signUpWorker(String email, String password, String name, String surname, int salary){
        User user = new Worker(email, PasswordHasher.hashPassword(password),name,surname, salary);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(user);
        db.getTransaction().commit();
        return user;
    }

    public User getUserByEmail(String email){
        User user;
        try{
            TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.email = ?1", User.class);
            query.setParameter(1, email);
            user = query.getSingleResult();
        }catch(NoResultException e){
            logger.error(String.format("There are no results related to %s email", email));
            user = null;
        }
        return user;
    }

    public List<ShowTime> getShowTimesByDate(LocalDate date){
        List<ShowTime> showtimes;
        try{
            TypedQuery<ShowTime> query = db.createQuery("SELECT s FROM ShowTime s WHERE s.screeningDate = ?1", ShowTime.class);
            query.setParameter(1, date);
            showtimes = query.getResultList();
        }catch(NoResultException e){
            logger.error(String.format("There are no results related to '%s' date", date));
            showtimes = null;
        }
        return showtimes;
    }


    public List<ShowTime> getShowTimesByDateAndFilm(LocalDate date, Film film){
        List<ShowTime> showtimes;
        try{
            TypedQuery<ShowTime> query = db.createQuery("SELECT s FROM ShowTime s WHERE s.screeningDate = ?1 AND s.film = ?2", ShowTime.class);
            query.setParameter(1, date);
            query.setParameter(2, film);
            showtimes = query.getResultList();
        }catch(NoResultException e){
            logger.error(String.format("There are no results related to '%s' date and '%s' film", date, film));
            showtimes = null;
        }
        return showtimes;
    }

    public List<PurchaseReceipt> getPurchaseReceiptsByUser(Customer customer){
        List<PurchaseReceipt> purchaseReceipts;
        try{
            TypedQuery<PurchaseReceipt> query = db.createQuery("SELECT p FROM PurchaseReceipt p WHERE p.customer = ?1", PurchaseReceipt.class);
            query.setParameter(1, customer);
            purchaseReceipts = query.getResultList();
        }catch(NoResultException e){
            logger.info(String.format("There are no results related to '%s' customer", customer.getEmail()));
            purchaseReceipts = null;
        }
        return purchaseReceipts;
    }


    public Schedule getScheduleByRoomAndDate(LocalDate date, ScreeningRoom screeningRoom) {
        Schedule schedule;
        try {
            TypedQuery<Schedule> query = db.createQuery(
                    "SELECT s FROM Schedule s WHERE s.id.date = :date AND s.id.screeningRoom = :room", Schedule.class);
            query.setParameter("date", date);
            query.setParameter("room", screeningRoom);

            schedule = query.getSingleResult();
            if (schedule != null) {
                schedule.reconstructBookingStateFromShowTimes();
            }
        } catch (NoResultException e) {
            logger.error(String.format("There are no results related to %s screeningRoom and %s date",
                    screeningRoom.getRoomNumber(), date));
            schedule = null;
        }
        return schedule;
    }


    public Schedule createSchedule(LocalDate date, ScreeningRoom screeningRoom){
        Schedule schedule = new Schedule(date, screeningRoom);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(schedule);
        db.getTransaction().commit();
        return schedule;
    }

    public void createShowTime(Schedule schedule, LocalTime screeningTime, Film film){
        ShowTime showTime = new ShowTime(schedule, screeningTime, film);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(showTime);
        db.getTransaction().commit();
    }


    public void createPurchaseReceipt(Customer customer, ShowTime showTime, List<Seat> seats){
        PurchaseReceipt purchaseReceipt = new PurchaseReceipt(new Date(), customer, showTime, seats);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(purchaseReceipt);
        for(Seat seat: seats){
            seat.setType(SeatType.OCCUPIED);
            db.merge(seat);
        }
        db.getTransaction().commit();
    }


    public void close() {
        if (db != null && db.isOpen()) db.close();
        if (emf != null && emf.isOpen()) emf.close();
        logger.info("DataBase is closed.");
    }

    public List<ScreeningRoom> getScreeningRooms() {
        try{
            TypedQuery<ScreeningRoom> query = db.createQuery("SELECT c.screeningRooms FROM Cinema c", ScreeningRoom.class);
            return query.getResultList();
        }catch (NoResultException e){
            logger.info("There are no results with the query");
            return null;
        }
    }

    public void storeReview(Film film, int rating, String textReview, Customer author){
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(new Review(film, rating, textReview, author));
        logger.debug("Review stored successfully");
        db.getTransaction().commit();
    }

    public Film storeFilm(String name){
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        Film film = FilmDataFetcher.fetchFilmDataByName(name);
        if(film == null){
            logger.error("There was an error while fetching the film data");
            return null;
        }
        db.persist(film);
        logger.debug("Film stored successfully");
        db.getTransaction().commit();
        return film;
    }

    public ShowTime storeShowtime(Schedule schedule, LocalTime screeningTime, Film film){
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        ShowTime showTime = new ShowTime(schedule, screeningTime, film);
        db.persist(showTime);
        logger.debug("Showtime stored successfully");
        db.getTransaction().commit();
        schedule.setShowTime(showTime);
        db.merge(schedule);
        return showTime;
    }

    public void saveShowTime(ShowTime showTime){
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(showTime);
        db.getTransaction().commit();
        Schedule schedule = showTime.getSchedule();
        schedule.setShowTime(showTime);
        db.merge(schedule);
    }

    public Double getAverageRating(Film film){
        Double average;
        try{
            TypedQuery<Double> query = db.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedFilm = ?1", Double.class);
            query.setParameter(1, film);
            average = query.getSingleResult();
        }catch(NoResultException e){
            logger.info(String.format("There are no ratings related to %s film", film.getTitle()));
            average = null;
        }
        return average;
    }

    public Review getReviewByFilmAndUser(Film film, User user) {
        Review review;
        try {
            TypedQuery<Review> query = db.createQuery(
                    "SELECT r FROM Review r WHERE r.reviewedFilm = :film AND r.author = :user",
                    Review.class
            );
            query.setParameter("film", film);
            query.setParameter("user", user);

            review = query.getSingleResult();
        } catch (NoResultException e) {
            logger.info(String.format("No review found for film '%s' and user '%s'", film.getTitle(), user.getEmail()));
            review = null;
        }
        return review;
    }

    public List<Review> getReviewsByFilm(Film film){
        List<Review> reviews;
        try{
            TypedQuery<Review> query = db.createQuery("SELECT r FROM Review r WHERE r.reviewedFilm = ?1", Review.class);
            query.setParameter(1, film);
            reviews = query.getResultList();
        }catch(NoResultException e){
            logger.info(String.format("There are no reviews related to %s film", film.getTitle()));
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    public List<Worker> getAllWorkers(){
        List<Worker>workers;
        try{
            TypedQuery<Worker>query=db.createQuery("SELECT w FROM Worker w", Worker.class);
            workers=query.getResultList();
        }catch (NoResultException e){
            logger.info("There are no workers in the database");
            workers = null;
        }
        return workers;
    }

    public void deleteWorker(Worker worker){
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.remove(worker);
        db.getTransaction().commit();
    }

    public List<Film> getAllFilms() {
        List<Film> films;
        try {
            TypedQuery<Film> query = db.createQuery("SELECT f FROM Film f", Film.class);
            films = query.getResultList();
        } catch (NoResultException e) {
            logger.info("There are no films in the database");
            films = null;
        }
        return films;
    }

    public void addFilm(Film film) {
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(film);
        db.getTransaction().commit();
    }
    public Film getFilmbyName(String name) {
        Film film;
        try {
            TypedQuery<Film> query = db.createQuery("SELECT f FROM Film f WHERE f.title = ?1", Film.class);
            query.setParameter(1, name);
            film = query.getSingleResult();
        } catch (NoResultException e) {
            logger.info(String.format("There are no films related to %s title", name));
            film = null;
        }
        return film;
    }

    public List<ShowTime>getAllShowtimes(){
        List<ShowTime> showtimes;
        try{
            TypedQuery<ShowTime> query = db.createQuery("SELECT s FROM ShowTime s", ShowTime.class);
            showtimes = query.getResultList();
        }catch(NoResultException e){
            logger.info("There are no showtimes in the database");
            showtimes = null;
        }
        return showtimes;
    }
    private void generateTestingData() {

        Cinema cinema = new Cinema("Cineflix", "Bilbo", 688861291, LocalTime.of(15, 30), LocalTime.of(01, 00));
        db.persist(cinema);

        Admin admin = new Admin("juanan.pereira@ehu.eus", PasswordHasher.hashPassword("admin1234"), "Juanan", "Pereira", 2500);
        db.persist(admin);

        ScreeningRoom screeningRoom1 = new ScreeningRoom(cinema,1);
        ScreeningRoom screeningRoom2 = new ScreeningRoom(cinema,2);
        ScreeningRoom screeningRoom3 = new ScreeningRoom(cinema,3);
        db.persist(screeningRoom1);
        db.persist(screeningRoom2);
        db.persist(screeningRoom3);

        signUpWorker("bercibengoa001@ikasle.ehu.eus", "12345678", "Beñat", "Ercibengoa", 2000);
        signUpWorker("vandrushkiv001@ikasle.ehu.eus", "87654321", "Viktoria", "Andrushkiv", 2000);
        signUpWorker("trolland001@ikasle.ehu.eus", "abcdefghi", "Théo", "Rolland", 2000);
        signUpWorker("lrodriguez154@ikasle.ehu.eus", "12345678", "Laura", "Rodríguez", 2000);
        signUpWorker("eugarte001@ikasle.ehu.eus", "111222333g", "Ekhi", "Ugarte", 2000);

        Customer customer1 = (Customer)signUp("aitor@gmail.com", "12345", "Aitor", "Elizondo");
        Customer customer2 = (Customer)signUp("amaia@gmail.com", "12345", "Amaia", "Susperregi");
        Customer customer3 = (Customer)signUp("uxue@gmail.com", "12345", "Uxue", "Etxebeste");


        Film film1 = storeFilm("The Godfather");
        Film film2 = storeFilm("Spirited Away");
        Film film3 = storeFilm("Cars");
        Film film4 = storeFilm("Fight Club");
        Film film5 = storeFilm("Shutter Island");


        ShowTime showTime1 = storeShowtime(getScheduleByRoomAndDate(LocalDate.now(), screeningRoom1), LocalTime.of(17, 00), film1);
        ShowTime showTime2 = storeShowtime(getScheduleByRoomAndDate(LocalDate.now(), screeningRoom2), LocalTime.of(18, 30), film2);
        ShowTime showTime3 = storeShowtime(getScheduleByRoomAndDate(LocalDate.now(), screeningRoom3), LocalTime.of(16, 00), film2);
        ShowTime showTime4 = storeShowtime(getScheduleByRoomAndDate(LocalDate.now(), screeningRoom1), LocalTime.of(20, 30), film3);
        ShowTime showTime5 = storeShowtime(getScheduleByRoomAndDate(LocalDate.now(), screeningRoom2), LocalTime.of(21, 30), film4);
        ShowTime showTime6 = storeShowtime(getScheduleByRoomAndDate(LocalDate.now(), screeningRoom3), LocalTime.of(18, 45), film5);


        List<Seat> seatSelection1 = new ArrayList<>();
        List<Seat> seatSelection2 = new ArrayList<>();
        List<Seat> seatSelection3 = new ArrayList<>();
        List<Seat> seatSelection4 = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            seatSelection1.add(screeningRoom1.getSeats().get(i));
        }
        for(int i = 21; i < 25; i++){
            seatSelection2.add(screeningRoom1.getSeats().get(i));
        }
        for(int i = 28; i < 35; i++){
            seatSelection3.add(screeningRoom1.getSeats().get(i));
        }
        for(int i = 37; i < 38; i++){
            seatSelection4.add(screeningRoom1.getSeats().get(i));
        }



        for(Seat seat: screeningRoom1.getSeats()){
            db.persist(seat);
        }
        for(Seat seat: screeningRoom1.getSeats()){
            db.persist(seat);
        }
        createPurchaseReceipt(customer1, showTime1, seatSelection1);
        createPurchaseReceipt(customer1, showTime1, seatSelection1);
        createPurchaseReceipt(customer2, showTime3, seatSelection1);

        storeReview(film1, 5, "Great film! Nothing similar has been seen recently", customer1);
        storeReview(film1, 1, "Interesting film", customer2);
        storeReview(film1, 2, "Nice", customer3);

    }


}