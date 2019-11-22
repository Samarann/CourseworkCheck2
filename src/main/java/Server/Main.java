package Server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main{

    public static Connection db = null;

    public static void main(String[] args){

        openUsers("Users.db"); //Opens the access to the database to be read to.
        serverConfiguration();



        closeDatabase(); //Closes the access to the database to be read to, so that it doesn't take up resources when it isn't being used.
        //Controllers.Users.createUsers();
    }

    //@Path("users/")
    public static void openUsers(String dbFile){ //Function to open the database.
        try{
            Class.forName("org.sqlite.JDBC"); //Loads the database driver.
            SQLiteConfig config = new SQLiteConfig(); //Loads the database settings.
            config.enforceForeignKeys(true); //Loads the database settings.
            Main.db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties()); //Open the database files.
            System.out.println("Database connection successfully established.");
        }catch (Exception exception){
            System.out.println("Database connection error: " + exception.getMessage());
        }
    }

    public static void serverConfiguration(){
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
        }
    }

    //@GET
    //@Path("close/")
    //@Produces(MediaType.APPLICATION_JSON)
    public static void closeDatabase(){ //Function to close the database.
        try{
            Main.db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception){
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }
}


