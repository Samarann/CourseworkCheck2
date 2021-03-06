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
import java.util.UUID;

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
    public static String createUsers(@FormDataParam("UserID") Integer UserIDAdd, @FormDataParam("UserEmail") String UserEmailAdd, @FormDataParam("UserName") String UserNameAdd, @FormDataParam("UserPass") String UserPassAdd, @CookieParam("token") String token){
        System.out.println("users/create/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
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
    public static String deleteUsers(@FormDataParam("UserID") Integer UserID, @CookieParam("token") String token){
        System.out.println("users/delete/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
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
    public String updateUsers(@FormDataParam("UserIDUp") Integer UserIDUp, @FormDataParam("UserNameUp") String UserNameUp, @FormDataParam("UserEmailUp") String UserEmailUp, @FormDataParam("UserPassUp") String UserPassUp, @CookieParam("token") String token){
        System.out.println("users/update/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try{
            if(UserIDUp == null ||  UserNameUp == null || UserEmailUp == null || UserPassUp == null){
                throw new Exception("One or more data form parameters are missing from the HTTP request.");
            }
            System.out.println("users/update UserID=" + UserIDUp);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UserName = ?, UserEmail = ?, UserPass = ?, UserAdmin = ? WHERE UserID = ?");

            ps.setString(1, UserNameUp);
            ps.setString(2, UserEmailUp);
            ps.setString(3, UserPassUp);
            ps.setInt(5, UserIDUp);

            if(UserNameUp.substring(0, 4).equals("aots")) {
                ps.setBoolean(4, true);
            }else{
                ps.setBoolean(4, false);
            }

            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("UserName") String username, @FormDataParam("UserPass") String password) {
        System.out.println("users/login");
        try {
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserPass FROM Users WHERE UserName = ?");
            ps1.setString(1, username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {
                String correctPassword = loginResults.getString(1);
                System.out.println(correctPassword);
                if (password.equals(correctPassword)) {
                    String token = UUID.randomUUID().toString();
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE UserName = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, username);
                    ps2.executeUpdate();
                    JSONObject userDetails = new JSONObject();
                    userDetails.put("username", username);
                    userDetails.put("token", token);
                    return userDetails.toString();
                } else {
                    return "{\"error\": \"Incorrect password.\"}";
                }
            } else {
                return "{\"error\": \"Unknown user.\"}";
            }
        } catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error.\"}";
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {
        System.out.println("users/logout");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try {
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {
                int id = logoutResults.getInt(1);
                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();
                return "{\"status\": \"OK\"}";
            } else {
                return "{\"error\": \"Invalid token!\"}";
            }
        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return false;
        }
    }
}
