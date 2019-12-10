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

@Path("factors/")
public class Factors{
    @GET
    @Path("read/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String readFactors(){
        System.out.println("factors/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Factors");
            ResultSet results = ps.executeQuery();
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("FactorID", results.getInt(1));
                item.put("FactorName", results.getString(2));
                item.put("FactorEffect", results.getString(3));
                read.add(item);
                System.out.println(results.getString("FactorID") + " | " + results.getString("FactorName") + " | " + results.getString("FactorEffect"));
            }
            return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\"; \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("create/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String createFactors(@FormDataParam("FactorIDAdd") Integer FactorIDAdd, @FormDataParam("FactorNameAdd") String FactorNameAdd, @FormDataParam("FactorEffectAdd") String FactorEffectAdd) {
        System.out.println("factors/create/");
        JSONArray read = new JSONArray();
        try {
            if (FactorNameAdd == null || FactorEffectAdd == null) {
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("factors/create FactorID=" + FactorIDAdd);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Factors (FactorID, FactorName, FactorEffect) VALUES (?, ?, ?)");

            ps.setInt(1, FactorIDAdd);
            ps.setString(2, FactorNameAdd);
            ps.setString(3, FactorEffectAdd);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";
            //read.toString();

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteFactors(@FormDataParam("FactorID") Integer FactorID){
        try{
            if (FactorID == null){
                throw new Exception("The ID data parameter is missing from the HTTP request.");
            }
            System.out.println("factors/delete FactorID=" + FactorID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Factors WHERE FactorID = ?");
            ps.setInt(1, FactorID);
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
    public String updateFactors(@FormDataParam("FactorIDUp") Integer FactorIDUp, @FormDataParam("FactorNameUp") String FactorNameUp, @FormDataParam("FactorEffectUp") String FactorEffectUp){
        System.out.println("users/update/");
        try{
            if(FactorIDUp == null ||  FactorNameUp == null || FactorEffectUp == null){
                throw new Exception("One or more data form parameters are missing from the HTTP request.");
            }
            System.out.println("factors/update FactorIDUp=" + FactorIDUp);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Factors SET FactorName = ?, FactorEffect = ? WHERE FactorID = ?");

            ps.setString(1, FactorNameUp);
            ps.setString(2, FactorEffectUp);
            ps.setInt(3, FactorIDUp);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}
