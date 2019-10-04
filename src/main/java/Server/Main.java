package Server;

import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main{

    public static Connection db = null;

    public static void main(String[] args){

        openDatabase("Users.db");

        ResourceConfig config = new ResourceConfig();
        config.packages("Controllers");
        config.register(MultiPartFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            System.out.println("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }


        UserClass.openDatabase("Users.db"); //Opens the access to the database to be read to.
        UserClass.writeDatabase();
        UserClass.readDatabase();
        UserClass.closeDatabase(); //Closes the access to the database to be read to, so that it doesn't take up resources when it isn't being used.
    }
}


