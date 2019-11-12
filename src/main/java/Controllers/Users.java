package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.sqlite.SQLiteConfig;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Users{
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

   // @GET
   // @Path("read/")
    //@Produces(MediaType.APPLICATION_JSON)
    public static void readUsers(){
        System.out.println("admin/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Users");
            ResultSet results = ps.executeQuery();
            System.out.println("UserID | UserEmail | UserName | UserPass | UserAdmin");
            while (results.next()){
                int UserID = results.getInt(1);
                String UserEmail = results.getString(2);
                String UserName = results.getString(3);
                String UserPass = results.getString(4);
                boolean UserAdmin = results.getBoolean(5); //ALWAYS COMING OUT AS FALSE. TEST AND FIX
                System.out.println(UserID + " | " + UserEmail + " | " + UserName + " | " + UserPass + " | " + UserAdmin);
            }
            //return read.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }

 //   @POST
 //   @Path("create/")
 //   @Consumes(MediaType.MULTIPART_FORM_DATA)
  //  @Produces(MediaType.APPLICATION_JSON)
    public String createUsers(){
        System.out.println("users/create/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserID, UserEmail, UserName, UserPass, UserAdmin) VALUES (?, ?, ?, ?, ?)");

            Scanner sc = new Scanner(System.in);

            System.out.println("Please input username");
            String UsernameAdd = sc.nextLine();

            System.out.println("Please input your email");
            String EmailAdd = sc.nextLine();

            System.out.println("Please input password");
            String PasswordAdd = sc.nextLine();

            //int UserIDAdd = ;

            //if(UsernameAdd.substring(0, 4).equals("aots")) { //BROKEN UNTIL USERADMIN FIXED
            //    ps.setString(5, true);
           // }else{
           //     ps.setString(5,false);
           // }

            //ps.setInt(1, UserIDAdd);
            ps.setString(2, EmailAdd);
            ps.setString(3, UsernameAdd);
            ps.setString(4, PasswordAdd);

            ps.executeUpdate();
            return read.toString();

        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create user. Please see your server console for more information.\"}";
        }
    }

    //@GET
    //@Path("close/")
    //@Produces(MediaType.APPLICATION_JSON)
    public static void closeUsers(){ //Function to close the database.
        try{
            Main.db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception){
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }
}