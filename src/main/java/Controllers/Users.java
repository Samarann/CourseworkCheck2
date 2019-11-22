package Controllers;


import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

@Path("Users/")
public class Users{
    @GET
    @Path("read/")
    @Produces(MediaType.APPLICATION_JSON)
    public String readUsers(){
        System.out.println("admin/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Users");
            ResultSet results = ps.executeQuery();
            //System.out.println("UserID | UserName | UserEmail | UserPass | UserAdmin");
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("UserEmail", results.getString(2));
                item.put("UserName", results.getString(3));
                item.put("UserPass", results.getString(4));
                item.put("UserAdmin", results.getBoolean(5));
                read.add(item);
                //System.out.println(UserID + " | " + UserEmail + " | " + UserName + " | " + UserPass + " | " + UserAdmin);
            }
            return read.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to read user. Please see your server console for more information.\"}";
        }
    }

 //   @POST
 //   @Path("create/")
 //   @Consumes(MediaType.MULTIPART_FORM_DATA)
  //  @Produces(MediaType.APPLICATION_JSON)
    public static void createUsers(){
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

            if(UsernameAdd.substring(0, 4).equals("aots")) { //BROKEN UNTIL USERADMIN FIXED
                ps.setBoolean(5, true);
            }else{
                ps.setBoolean(5, false);
            }

            ps.setString(2, EmailAdd);
            ps.setString(3, UsernameAdd);
            ps.setString(4, PasswordAdd);

            ps.executeUpdate();
            read.toString();

        } catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void deleteUsers(int UserID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}