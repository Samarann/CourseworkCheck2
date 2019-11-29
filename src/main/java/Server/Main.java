package Server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {

        openDatabase("Users.db");

        ResourceConfig config = new ResourceConfig(); //Prepares Jersey servlet.
        config.packages("Controllers");
        config.register(MultiPartFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8081); //Makes Jetty server listen to port 8081.
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*"); //Connects servlet to server.

        try {
            server.start();
            System.out.println("Server successfully started."); //Success messaging to ensure it works.
            server.join();
        } catch (Exception e) {
            e.printStackTrace();

            //Controllers.Users.readUsers();
            //Controllers.Users.createUsers(8, "imauser@createduser.com", "CreatedUser", "1m4n3wus3r");
            //Controllers.Users.readUsers();
        }
    }


    public static void openDatabase(String dbFile) { //Function to open the database.
        try {
            Class.forName("org.sqlite.JDBC"); //Loads the database driver.
            SQLiteConfig config = new SQLiteConfig(); //Loads the database settings.
            config.enforceForeignKeys(true); //Loads the database settings.
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties()); //Open the database files.
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }
    }
}