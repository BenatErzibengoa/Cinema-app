package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.domain.users.User;

/**
 * Interface that specifies the business logic.
 */
public interface BlFacade  {

    // ===== Define the public interface of the BL

    /**
     * Attempts to log in a user with the given credentials
     * @param email the username
     * @param password the password
     * @return User if login successful, null otherwise
     */
    User login(String email, String password);


}
