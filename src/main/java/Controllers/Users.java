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

@Path("users/")
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
    public static String createUsers(@FormDataParam("UserIDAdd") Integer UserIDAdd, @FormDataParam("UserEmailAdd") String UserEmailAdd, @FormDataParam("UserNameAdd") String UserNameAdd, @FormDataParam("UserPassAdd") String UserPassAdd){
        System.out.println("users/create/");
        JSONArray read = new JSONArray();
        try{
            if(UserEmailAdd == null || UserNameAdd == null || UserPassAdd == null){
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("users/create UserID=" + UserIDAdd);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserID, UserEmail, UserName, UserPass, UserAdmin) VALUES (?, ?, ?, ?, ?)");

            if(UserNameAdd.substring(0, 4).equals("aots")) {
                ps.setBoolean(5, true);
            }else{
                ps.setBoolean(5, false);
            }

            ps.setInt(1, UserIDAdd);
            ps.setString(2, UserEmailAdd);
            ps.setString(3, UserNameAdd);
            ps.setString(4, UserPassAdd);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";
            //read.toString();

        } catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteUsers(@FormDataParam("UserID") Integer UserID){
        try{
            if (UserID == null){
                throw new Exception("The ID data parameter is missing from the HTTP request.");
            }
            System.out.println("user/delete UserID=" + UserID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.execute();

            return "{\"status\": \"OK\"}";

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUsers(@FormDataParam("UserIDUp") Integer UserIDUp, @FormDataParam("UserNameUp") String UserNameUp, @FormDataParam("UserEmailUp") String UserEmailUp, @FormDataParam("UserPassUp") String UserPassUp){
        System.out.println("users/update/");
        try{
            if(UserIDUp == null ||  UserNameUp == null || UserEmailUp == null || UserPassUp == null){
                throw new Exception("One or more data form parameters are missing from the HTTP request.");
            }
            System.out.println("users/update UserID=" + UserIDUp);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UserName = ?, UserEmail = ?, UserPass = ?, UserAdmin = ? WHERE UserID = ?");

            if(UserNameUp.substring(0, 4).equals("aots")) {
                ps.setBoolean(5, true);
            }else{
                ps.setBoolean(5, false);
            }

            ps.setInt(1, UserIDUp);
            ps.setString(2, UserNameUp);
            ps.setString(3, UserEmailUp);
            ps.setString(4, UserPassUp);

            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}