package eus.ehu.cinemaProject.dataAccess;

import eus.ehu.cinemaProject.configuration.Config;
import eus.ehu.cinemaProject.domain.users.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;



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

    public void signUp(String userName, String password){
        User user = new User(userName, password, "Customer");
        db.getTransaction().begin();
        db.persist(user);
        db.getTransaction().commit();
    }


    private void generateTestingData() {
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