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

@Path("folders/")
public class Folders {
    @GET
    @Path("read/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String readFolders(){
        System.out.println("folders/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Folders");
            ResultSet results = ps.executeQuery();
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("FolderID", results.getInt(1));
                item.put("FolderName", results.getString(2));
                read.add(item);
                System.out.println(results.getString("FolderID") + " | " + results.getString("FolderName"));
            }
            return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\"; \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteFolders(@FormDataParam("FolderID") Integer FolderID, @FormDataParam("FolderName") String FolderName, @CookieParam("token") String token){
        System.out.println("folders/delete/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try{
            if (FolderID == null){
                throw new Exception("The ID data parameter is missing from the HTTP request.");
            }
            System.out.println("folders/delete FolderID=" + FolderID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Folders WHERE FolderID = ?");
            ps.setInt(1, FolderID);
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
    public String updateFolders(@FormDataParam("FolderIDUp") Integer FolderIDUp, @FormDataParam("FolderNameUp") String FolderNameUp, @CookieParam("token") String token){
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try{
            if(FolderIDUp == null ||  FolderNameUp == null){
                throw new Exception("One or more data form parameters are missing from the HTTP request.");
            }
            System.out.println("folders/update FolderIDUp=" + FolderIDUp);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Folders SET FolderName = ? WHERE FolderID = ?");

            ps.setString(1, FolderNameUp);
            ps.setInt(2, FolderIDUp);
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
    public static String createFolders(@FormDataParam("FolderIDAdd") Integer FolderIDAdd, @FormDataParam("FolderNameAdd") String FolderNameAdd, @CookieParam("token") String token) {
        System.out.println("folders/create/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        JSONArray read = new JSONArray();
        try {
            if (FolderIDAdd == null || FolderNameAdd == null) {
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("folders/create FolderIDAdd=" + FolderIDAdd);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Folders (FolderID, FolderName) VALUES (?, ?)");

            ps.setInt(1, FolderIDAdd);
            ps.setString(2, FolderNameAdd);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";
            //read.toString();

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}
