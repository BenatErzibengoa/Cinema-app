package eus.ehu.cinemaProject.dataAccess;
import org.h2.tools.Server;

public class H2WebLauncher {
    public static void main(String[] args) {
        try {
            // Start the H2 web server with default settings
            Server webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082").start();

            System.out.println("H2 Web Console server started.");
            System.out.println("URL: " + webServer.getURL());
            System.out.println("Press Ctrl+C to stop the server...");

        } catch (Exception e) {
            System.out.println("Error starting H2 server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
