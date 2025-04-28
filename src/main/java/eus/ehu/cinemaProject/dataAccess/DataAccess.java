package eus.ehu.cinemaProject.dataAccess;

import eus.ehu.cinemaProject.configuration.Config;
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

    public User login(String email, String password){
        User user;
        try{
            TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.email = ?1 AND u.password = ?2", User.class);
            query.setParameter(1, email);
            query.setParameter(2, password);
            user = query.getSingleResult();
        }catch(NoResultException e){
            logger.error(String.format("There are no results related to %s %s email and password", email, password));
            user = null;
        }
        return user;
    }

    public void signUp(String email, String password, String name, String surname){
        User user = new Customer(email,password,name,surname);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(user);
        db.getTransaction().commit();
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
        } catch (NoResultException e) {
            logger.error(String.format("There are no results related to %s screeningRoom and %s date", screeningRoom.getRoomNumber(), date.toString()));
            schedule = null;
        }
        return schedule;
    }


    public void createSchedule(LocalDate date, ScreeningRoom screeningRoom){
        Schedule schedule = new Schedule(date, screeningRoom);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(schedule);
        db.getTransaction().commit();
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
        db.getTransaction().commit();
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


    private void generateTestingData() {

        Cinema cinema = new Cinema("Cineflix", "Bilbo", 688861291, LocalTime.of(15, 30), LocalTime.of(01, 00));

        Admin admin = new Admin("juanan.pereira@ehu.eus", "admin1234", "Juanan", "Pereira", 2500);

        Worker worker1 = new Worker("bercibengoa001@ikasle.ehu.eus", "12345678", "Beñat", "Ercibengoa", 2000);
        Worker worker2 = new Worker("vandrushkiv001@ikasle.ehu.eus", "87654321", "Viktoria", "Andrushkiv", 2000);
        Worker worker3 = new Worker("trolland001@ikasle.ehu.eus", "abcdefghi", "Théo", "Rolland", 2000);
        Worker worker4 = new Worker("lrodriguez154@ikasle.ehu.eus", "12345678", "Laura", "Rodríguez", 2000);
        Worker worker5 = new Worker("eugarte001@ikasle.ehu.eus", "111222333g", "Ekhi", "Ugarte", 2000);

        Customer customer1 = new Customer("aitor@gmail.com", "12345", "Aitor", "Elizondo");
        Customer customer2 = new Customer("amaia@gmail.com", "12345", "Amaia", "Susperregi");
        Customer customer3 = new Customer("uxue@gmail.com", "12345", "Uxue", "Etxebeste");

        List<Genre> genreList1 = new ArrayList<>();
        genreList1.add(Genre.DRAMA);
        Film film1 = new Film("The Godfather", "Francis Ford Coppola", LocalTime.of(2, 55),
                "A cinematic masterpiece directed by Francis Ford Coppola",
                genreList1, "/eus/ehu/cinemaProject/ui/pictures/the-godfather.jpg");
        film1.setImagePath("/eus/ehu/cinemaProject/ui/pictures/the-godfather.jpg");

        List<Genre> genreList2 = new ArrayList<>();
        genreList2.add(Genre.ACTION);
        genreList2.add(Genre.ADVENTURE);
        Film film2 = new Film("Die Hard", "John McTiernan", LocalTime.of(2, 11),
                "An action-packed thriller directed by John McTiernan",
                genreList2,
                "/eus/ehu/cinemaProject/ui/pictures/die-hard.jpg");
        film2.setImagePath("/eus/ehu/cinemaProject/ui/pictures/die-hard.jpg");

        Film film3 = FilmDataFetcher.fetchFilmDataByName("Cars");
        logger.debug(film3);

        ScreeningRoom screeningRoom1 = new ScreeningRoom(cinema,1);
        ScreeningRoom screeningRoom2 = new ScreeningRoom(cinema,2);
        ScreeningRoom screeningRoom3 = new ScreeningRoom(cinema,3);



        LocalDate today = LocalDate.now();  // today
        Schedule schedule1 = new Schedule(today, screeningRoom1);
        Schedule schedule2 = new Schedule(today, screeningRoom2);
        Schedule schedule3 = new Schedule(today, screeningRoom3);


        ShowTime showTime1 = new ShowTime(schedule1, LocalTime.of(17, 00), film1);
        ShowTime showTime2 = new ShowTime(schedule2, LocalTime.of(18, 30), film2);
        ShowTime showTime3 = new ShowTime(schedule2, LocalTime.of(16, 00), film2);
        ShowTime showTime4 = new ShowTime(schedule1, LocalTime.of(20, 00), film1);
        ShowTime showTime5 = new ShowTime(schedule2, LocalTime.of(20, 30), film2);

        ShowTime showTime6 = new ShowTime(schedule3, LocalTime.of(17, 00), film3);



        schedule1.setShowTime(showTime1);
        schedule2.setShowTime(showTime2);
        schedule3.setShowTime(showTime3);

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


        db.persist(cinema);
        db.persist(admin);
        db.persist(worker1);
        db.persist(worker2);
        db.persist(worker3);
        db.persist(worker4);
        db.persist(worker5);
        db.persist(customer1);
        db.persist(customer2);
        db.persist(customer3);
        db.persist(film1);
        db.persist(film2);
        db.persist(film3);
        db.persist(screeningRoom1);
        db.persist(screeningRoom2);
        db.persist(screeningRoom3);
        db.persist(schedule1);
        db.persist(schedule2);
        db.persist(schedule3);
        for(Seat seat: screeningRoom1.getSeats()){
            db.persist(seat);
        }
        for(Seat seat: screeningRoom1.getSeats()){
            db.persist(seat);
        }
        db.persist(showTime1);
        db.persist(showTime2);
        db.persist(showTime3);
        db.persist(showTime4);
        db.persist(showTime5);
        db.persist(showTime6);


        createPurchaseReceipt(customer1, showTime1, seatSelection1);
        createPurchaseReceipt(customer1, showTime1, seatSelection1);
        createPurchaseReceipt(customer2, showTime3, seatSelection1);

        LocalDate tomorrow = today.plusDays(1);  // Tomorrow
        createSchedule(tomorrow, screeningRoom1);
        createSchedule(tomorrow, screeningRoom2);

        storeReview(film1, 5, "Great film! Nothing similar has been seen recently", customer1);
        storeReview(film1, 1, "Interesting film", customer2);
        storeReview(film1, 2, "Nice", customer3);


        logger.info(getScheduleByRoomAndDate(tomorrow, screeningRoom1));  // Utilise 'tomorrow'
        createShowTime(getScheduleByRoomAndDate(tomorrow, screeningRoom1), LocalTime.of(17, 00), film1);
        logger.info("showtime created successfully");


        //This is made to assure we do the queries before persisting data
        //If not it attempts to print this before finishing persisting some data
        Task<Void> task = new Task<Void>(){
            protected Void call() throws Exception{
                Thread.sleep(500);
                logger.debug("Query testing:");
                logger.debug("Showtimes with date(yyyy/mm/dd) 2025/04/01:");
                for(ShowTime showtime: getShowTimesByDate(today)) {
                    logger.debug(showtime);
                }
                logger.debug("Showtimes with date(yyyy/mm/dd) 2025/04/01 and film:");
                for(ShowTime showtime: getShowTimesByDateAndFilm(today, film1)){
                    logger.debug(showtime);
                }
                return null;
            }
        };
        //new Thread(task).start();

    }


}