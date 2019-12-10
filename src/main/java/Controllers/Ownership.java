package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
public class Ownership {
    @GET
    @Path("saves/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String readOwnership(){
        System.out.println("users/saves/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Ownership");
            ResultSet results = ps.executeQuery();
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("SaveID", results.getInt(2));
                read.add(item);
                System.out.println(results.getString("UserID") + " | " + results.getString("SaveID"));
            }
            return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\"; \"Unable to list items, please see server console for more info.\"}";
        }
    }
}