package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.domain.users.User;

/**
 * Interface that specifies the business logic.
 */
public interface BlFacade  {

    // ===== Define the public interface of the BL

    /**
     * Attempts to log in a user with the given credentials
     * @param username the username
     * @param password the password
     * @param role the role of the user
     * @return User if login successful, null otherwise
     */
    User login(String username, String password, String role);


}
