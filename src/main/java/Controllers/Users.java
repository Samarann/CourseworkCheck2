package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.sqlite.SQLiteConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


@Path("users/")
public class Users{
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

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public static readUsers(){
        System.out.println("users/read");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, Password FROM Users");
            ResultSet results = ps.executeQuery();
            System.out.println("UserID | Username | Password");
            while (results.next()){
                int UserID = results.getInt(1);
                String Username = results.getString(2);
                String Password = results.getString(3);
                System.out.println(UserID + " | " + Username + " | " + Password);
            }
            return read.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items. Please see your server console for more information.\"}";
        }
    }

    public static void writeUsers(){
        try{

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserID, Username, Password) VALUES (?, ?, ?)");

            Scanner sc = new Scanner(System.in);

            System.out.println("Please input Username");
            String UsernameAdd = sc.nextLine();

            System.out.println("Please input Password");
            String PasswordAdd = sc.nextLine();

            //int UserIDAdd = ;

            //ps.setInt(1, UserIDAdd);
            ps.setString(2, UsernameAdd);
            ps.setString(3, PasswordAdd);

            ps.executeUpdate();

        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void closeUsers(){ //Function to close the database.
        try{
            Main.db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception){
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }
}