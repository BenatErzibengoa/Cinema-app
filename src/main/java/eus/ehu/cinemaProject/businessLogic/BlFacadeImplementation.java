package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.domain.ScreeningRoom;
import eus.ehu.cinemaProject.domain.Seat;
import eus.ehu.cinemaProject.domain.ShowTime;
import eus.ehu.cinemaProject.domain.users.Customer;
import eus.ehu.cinemaProject.domain.users.User;

import eus.ehu.cinemaProject.configuration.Config;
import eus.ehu.cinemaProject.dataAccess.DataAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    public User login(String email, String password){
        return dbManager.login(email, password);
    }

    public User getUserByEmail(String email){ return dbManager.getUserByEmail(email);}

    public void signUp(String email, String password, String name, String surname){
        dbManager.signUp(email,password,name,surname);
    }

    public List<ScreeningRoom> getScreeningRooms() {
        return dbManager.getScreeningRooms();
    }

}

