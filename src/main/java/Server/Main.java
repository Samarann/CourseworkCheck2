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

        Controllers.Users.openUsers("Users.db"); //Opens the access to the database to be read to.

        serverConfiguration();

        //Controllers.Users.createUsers();
        //Controllers.Users.readUsers();
        Controllers.Factors.readFactors();
        Controllers.Users.closeUsers(); //Closes the access to the database to be read to, so that it doesn't take up resources when it isn't being used.
        //Controllers.Users.createUsers();
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
            //server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


