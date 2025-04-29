package eus.ehu.cinemaProject.configuration;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Hash password with cost 12
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Verify password against the hashed password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
