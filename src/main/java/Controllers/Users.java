package Controllers;


import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

@Path("Users/")
public class Users{
    @GET
    @Path("read/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String readUsers(){
        System.out.println("users/read/");
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
                System.out.println(results.getString("UserID") + " | " + results.getString("UserEmail") + " | " + results.getString("UserName") + " | " + results.getString("UserPass") + " | " + results.getString("UserAdmin"));
            }
            return read.toString();
        }catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\"; \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("create/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static void createUsers(@FormDataParam("UserEmailAdd") String UserEmailAdd, @FormDataParam("UserNameAdd") String UserNameAdd, @FormDataParam("UserPassAdd") String UserPassAdd){
        System.out.println("users/create/");
        JSONArray read = new JSONArray();
        try{
            if(UserEmailAdd == null || UserNameAdd == null || UserPassAdd == null){
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("users/create UserID=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserID, UserEmail, UserName, UserPass, UserAdmin) VALUES (?, ?, ?, ?, ?)");

            Scanner sc = new Scanner(System.in);

            if(UserNameAdd.substring(0, 4).equals("aots")) {
                ps.setBoolean(5, true);
            }else{
                ps.setBoolean(5, false);
            }

            ps.setString(2, UserEmailAdd);
            ps.setString(3, UserNameAdd);
            ps.setString(4, UserPassAdd);

            ps.executeUpdate();
            read.toString();

        } catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void deleteUsers(int UserID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }
}