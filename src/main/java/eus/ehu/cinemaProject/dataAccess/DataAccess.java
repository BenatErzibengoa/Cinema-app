package eus.ehu.cinemaProject.dataAccess;

import eus.ehu.cinemaProject.configuration.Config;
import eus.ehu.cinemaProject.domain.Cinema;
import eus.ehu.cinemaProject.domain.Film;
import eus.ehu.cinemaProject.domain.Genre;
import eus.ehu.cinemaProject.domain.PurchaseReceipt;
import eus.ehu.cinemaProject.domain.users.Admin;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;
import eus.ehu.cinemaProject.domain.users.Worker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DataAccess {

    protected EntityManager db;
    protected EntityManagerFactory emf;

    public DataAccess() {

        this.open();

    }

    public void open() {
        open(false);
    }


    public void open(boolean initializeMode) {

        Config config = Config.getInstance();

        System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
                config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());

        String fileName = config.getDatabaseName();
        if (initializeMode) {
            fileName = fileName + ";drop";
            System.out.println("Deleting the DataBase");
        }

        if (config.isDataAccessLocal()) {
            System.out.println("Loading Hibernate configuration...");
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
            System.out.println("DataBase opened");
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
            System.out.println("The database has been initialized");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String email, String password){
        User user;
        try{
            this.open();
            TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.email = ?1 AND u.password = ?2", User.class);
            query.setParameter(1, email);
            query.setParameter(2, password);
            user = query.getSingleResult();
            this.close();
        }catch(NoResultException e){
            System.out.println(String.format("There are no results with %s %s email and password", email, password));
            user = null;
        }
        return user;
    }

    public void signUp(String email, String password, String name, String surname){
        this.open();

        User user = new Customer(email,password,name,surname);
        if (!db.getTransaction().isActive()) {
            db.getTransaction().begin();
        }
        db.persist(user);
        db.getTransaction().commit();

        this.close();
    }


    private void generateTestingData() {

        Cinema cinema = new Cinema("Cineflix", "Bilbo", 688861291);

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
        Film film1 = new Film("The Godfather", "Francis Ford Coppola", 1972,
                "A cinematic masterpiece directed by Francis Ford Coppola",
                genreList1);

        List<Genre> genreList2 = new ArrayList<>();
        genreList2.add(Genre.ACTION);
        genreList2.add(Genre.ADVENTURE);
        Film film2 = new Film("Die Hard", "John McTiernan", 1988,
                "An action-packed thriller directed by John McTiernan",
                genreList2);



        PurchaseReceipt purchaseReceipt1 = new PurchaseReceipt(new Date(), 75, 100.5, customer1, film1);
        PurchaseReceipt purchaseReceipt2 = new PurchaseReceipt(new Date(), 17, 100.5, customer1, film2);
        PurchaseReceipt purchaseReceipt3 = new PurchaseReceipt(new Date(), 23, 100.5, customer2, film2);
        PurchaseReceipt purchaseReceipt4 = new PurchaseReceipt(new Date(), 11, 100.5, customer3, film2);



        db.persist(cinema);
        db.persist(admin);

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

        db.persist(purchaseReceipt1);
        db.persist(purchaseReceipt2);
        db.persist(purchaseReceipt3);

        /*

        User user1 = new User("bercibengoa001@ikasle.ehu.eus", "12345678", "Beñat", "Ercibengoa");
        User user2 = new User("vandrushkiv001@ikasle.ehu.eus", "87654321", "Viktoria", "Andrushkiv ");
        User user3 = new User("trolland001@ikasle.ehu.eus", "abcdefghi", "Théo", "Rolland");
        User user4 = new User("lrodriguez154@ikasle.ehu.eus", "12345678", "Laura", "Rodríguez");
        User user5 = new User("ekhi100@ikasle.ehu.eus", "111222333g", "Ekhi", "?");

        db.persist(user1);
        db.persist(user2);
        db.persist(user3);
        db.persist(user4);
        db.persist(user5);


        /*

        User user = new User("bengoaerzi@gmail.com", "12345678", "Beñat", "Erzibengoa");

        User teacher1 = new User("john.smith", "pass123", "Teacher");
        User teacher2 = new User("mary.jones", "pass456", "Teacher");

        User student1 = new User("alex.brown", "student123", "Student");
        User student2 = new User("sarah.wilson", "student456", "Student");
        User student3 = new User("mike.davis", "student789", "Student");

        User admin1 = new User("admin.main", "admin123", "Administrative");
        User admin2 = new User("jane.doe", "admin456", "Administrative");


        db.persist(teacher1);
        db.persist(teacher2);
        db.persist(student1);
        db.persist(student2);
        db.persist(student3);
        db.persist(admin1);
        db.persist(admin2);
        */
    }



    public void close() {
        db.close();
        System.out.println("DataBase is closed");
    }

}