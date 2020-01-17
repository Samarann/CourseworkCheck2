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

@Path("saves/")
public class Saves {
    @GET
    @Path("read/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String readSaves(){
        System.out.println("saves/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT SaveID, SaveName FROM Saves");
            ResultSet results = ps.executeQuery();
            System.out.println("SaveID | SaveName");
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("SaveID", results.getInt(1));
                item.put("SaveName", results.getString(2));

                //int SaveID = results.getInt(1);
                // SaveName = results.getString(2);
                //System.out.println(SaveID + " | " + SaveName);
            }
            return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to list saves, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteSaves(@FormDataParam("SaveID") Integer SaveID, @CookieParam("token") String token){
        System.out.println("saves/delete/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try{
            if (SaveID == null){
                throw new Exception("The ID data parameter is missing from the HTTP request.");
            }
            System.out.println("folders/delete SaveID=" + SaveID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Saves WHERE SaveID = ?");
            ps.setInt(1, SaveID);
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
    public String updateSaves(@FormDataParam("SaveIDUp") Integer SaveIDUp, @FormDataParam("SaveNameUp") String SaveNameUp, @CookieParam("token") String token){
        System.out.println("saves/update/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try{
            if(SaveIDUp == null ||  SaveNameUp == null){
                throw new Exception("One or more data form parameters are missing from the HTTP request.");
            }
            System.out.println("saves/update SavesIDUp=" + SaveIDUp);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Saves SET SaveName = ? WHERE SaveID = ?");

            ps.setString(1, SaveNameUp);
            ps.setInt(2, SaveIDUp);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";

        } catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("create/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String createSaves(@FormDataParam("SaveIDAdd") Integer SaveIDAdd, @FormDataParam("SaveNameAdd") String SaveNameAdd, @CookieParam("token") String token) {
        System.out.println("saves/create/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        JSONArray read = new JSONArray();
        try {
            if (SaveIDAdd == null || SaveNameAdd == null) {
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("saves/create SaveIDAdd=" + SaveIDAdd);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Saves (SaveID, SaveName) VALUES (?, ?)");

            ps.setInt(1, SaveIDAdd);
            ps.setString(2, SaveNameAdd);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";
            //read.toString();

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}
