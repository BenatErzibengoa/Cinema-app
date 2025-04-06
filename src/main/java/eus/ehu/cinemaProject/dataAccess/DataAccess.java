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
import java.util.Set;


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
            logger.info(String.format("There are no results with %s %s email and password", email, password));
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
            logger.info(String.format("There are no results related with %s email", email));
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
            logger.info(String.format("There are no results related with '%s' date", date));
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
            logger.info(String.format("There are no results related with '%s' date and '%s' film", date, film));
            showtimes = null;
        }
        return showtimes;
    }



    public void close() {
        if (db != null && db.isOpen()) db.close();
        if (emf != null && emf.isOpen()) emf.close();
        logger.info("DataBase is closed.");
    }

    private void generateTestingData() {

        Cinema cinema = new Cinema("Cineflix", "Bilbo", 688861291, LocalTime.of(15, 30), LocalTime.of(01, 00));

        Admin admin = new Admin("juanan.pereira@ehu.eus", "admin1234", "Juanan", "Pereira", 2500);

        Worker worker1 = new Worker("bercibengoa001@ikasle.ehu.eus", "12345678", "Beñat", "Ercibengoa", 2000);
        Worker worker2 = new Worker("vandrushkiv001@ikasle.ehu.eus", "87654321", "Viktoria", "Andrushkiv", 2000);
        Worker worker3 = new Worker("trolland001@ikasle.ehu.eus", "abcdefghi", "Théo", "Rolland", 2000);
        Worker worker4 = new Worker("lrodriguez154@ikasle.ehu.eus", "12345678", "Laura", "Rodríguez", 2000);
        Worker worker5 = new Worker("eugarte001@ikasle.ehu.eus", "111222333g", "Ekhi", "Ugarte", 2000);

        Customer customer1 = new Customer("aitor@gmail.com", "11111", "Aitor", "Elizondo");
        Customer customer2 = new Customer("amaia@gmail.com", "22222", "Amaia", "Susperregi");
        Customer customer3 = new Customer("uxue@gmail.com", "33333", "Uxue", "Etxebeste");

        List<Genre> genreList1 = new ArrayList<>();
        genreList1.add(Genre.DRAMA);
        Film film1 = new Film("The Godfather", "Francis Ford Coppola", LocalTime.of(2, 55),
                "A cinematic masterpiece directed by Francis Ford Coppola",
                genreList1);

        List<Genre> genreList2 = new ArrayList<>();
        genreList2.add(Genre.ACTION);
        genreList2.add(Genre.ADVENTURE);
        Film film2 = new Film("Die Hard", "John McTiernan", LocalTime.of(2, 11),
                "An action-packed thriller directed by John McTiernan",
                genreList2);

        ScreeningRoom screeningRoom1 = new ScreeningRoom(cinema,1);
        ScreeningRoom screeningRoom2 = new ScreeningRoom(cinema,2);

        Schedule schedule1 = new Schedule(LocalDate.of(2025, 4, 1), screeningRoom1);
        Schedule schedule2 = new Schedule(LocalDate.of(2025, 4, 1), screeningRoom2);


        ShowTime showTime1 = new ShowTime(screeningRoom1, schedule1, LocalTime.of(17, 00), film1);
        ShowTime showTime2 = new ShowTime(screeningRoom2, schedule2, LocalTime.of(18, 30), film2);


        schedule1.setShowTime(showTime1);
        schedule2.setShowTime(showTime2);

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

        PurchaseReceipt purchaseReceipt1 = new PurchaseReceipt(new Date(),   customer1, showTime1, seatSelection1);
        PurchaseReceipt purchaseReceipt2 = new PurchaseReceipt(new Date(),  customer1, showTime2, seatSelection2);
        PurchaseReceipt purchaseReceipt3 = new PurchaseReceipt(new Date(),  customer2, showTime2, seatSelection3);
        PurchaseReceipt purchaseReceipt4 = new PurchaseReceipt(new Date(),  customer3, showTime2, seatSelection4);


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
        db.persist(screeningRoom1);
        db.persist(screeningRoom2);
        db.persist(schedule1);
        db.persist(schedule2);
        for(Seat seat: screeningRoom1.getSeats()){
            db.persist(seat);
        }
        for(Seat seat: screeningRoom1.getSeats()){
            db.persist(seat);
        }
        db.persist(showTime1);
        db.persist(showTime2);
        db.persist(purchaseReceipt1);
        db.persist(purchaseReceipt2);
        db.persist(purchaseReceipt3);
        db.persist(purchaseReceipt4);



        //This is made to assure we do the queries before persisting data
        //If not it attempts to print this before finishing persisting some data
        Task<Void> task = new Task<Void>(){
            protected Void call() throws Exception{
                Thread.sleep(500);
                logger.info("Query testing:");
                logger.info("Showtimes with date(yyyy/mm/dd) 2025/04/01:");
                for(ShowTime showtime: getShowTimesByDate(LocalDate.of(2025, 4, 1)) ) {
                    logger.info(showtime);
                }
                logger.info("Showtimes with date(yyyy/mm/dd) 2025/04/01 and film:");
                for(ShowTime showtime: getShowTimesByDateAndFilm(LocalDate.of(2025, 4, 1), film1) ) {
                    logger.info(showtime);
                }
                return null;
            }
        };
        new Thread(task).start();



    }


}