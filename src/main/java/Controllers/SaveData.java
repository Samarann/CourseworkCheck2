package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sqlite.SQLiteConfig;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

@Path("saves/")
public class SaveData {
    @GET
    @Path("data/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String readSaveData(){
        System.out.println("saves/data/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM SaveData");
            ResultSet results = ps.executeQuery();
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("SaveID", results.getInt(1));
                item.put("FactorID", results.getInt(2));
                read.add(item);
                System.out.println(results.getString("SaveID") + " | " + results.getString("FactorID"));
            }
            return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\"; \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("insert/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String insertSaveData(@FormDataParam("SaveID") Integer SaveID, @FormDataParam("FactorID") Integer FactorID, @CookieParam("token") String token){
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try {
            if (SaveID == null || FactorID == null) {
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("saves/insert SaveID=" + SaveID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO SaveData (SaveID, FactorID) VALUES (?, ?)");

            ps.setInt(1, SaveID);
            ps.setInt(2, FactorID);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";
            //read.toString();

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}