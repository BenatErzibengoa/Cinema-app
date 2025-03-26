package eus.ehu.cinemaProject.businessLogic;
import eus.ehu.cinemaProject.domain.users.User;

import eus.ehu.cinemaProject.configuration.Config;
import eus.ehu.cinemaProject.dataAccess.DataAccess;


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

}

