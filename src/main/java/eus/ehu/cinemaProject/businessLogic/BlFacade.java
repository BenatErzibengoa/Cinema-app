package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.domain.users.User;

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
     * Registers a customer with the given credentials and personal data
     *
     * @param email    the email
     * @param password the password
     * @param name     the name
     * @param surname  the surname
     */
    void signUp(String email, String password, String name, String surname);

}