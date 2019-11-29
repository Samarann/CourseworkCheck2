package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
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
    public static void readFactors(){
        System.out.println("factors/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Factors");
            ResultSet results = ps.executeQuery();
            System.out.println("FactorID | FactorName | FactorEffect");
            while (results.next()){
                int FactorID = results.getInt(1);
                String FactorName = results.getString(2);
                String FactorEffect = results.getString(3);
                System.out.println(FactorID + " | " + FactorName + " | " + FactorEffect);
            }
            //return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
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

            Scanner sc = new Scanner(System.in);

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
    @Path("delete")
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

}
