package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;

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
    public static void readFolders(){
        System.out.println("folders/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Folders");
            ResultSet results = ps.executeQuery();
            while (results.next()){
                int FolderID = results.getInt(1);
                String FolderName = results.getString(2);
                System.out.println(FolderID + " | " + FolderName);
            }
            //return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
        }
    }

    @POST
    @Path("delete/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteFolders(@FormDataParam("FolderID") Integer FolderID, @FormDataParam("FolderName") String FolderName){
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
    public String updateFolders(@FormDataParam("FolderIDUp") Integer FolderIDUp, @FormDataParam("FolderNameUp") String FolderNameUp){
        System.out.println("users/update/");
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
    public static String createFolders(@FormDataParam("FolderIDAdd") Integer FolderIDAdd, @FormDataParam("FolderNameAdd") String FolderNameAdd) {
        System.out.println("folders/create/");
        JSONArray read = new JSONArray();
        try {
            if (FolderIDAdd == null || FolderNameAdd == null) {
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("folders/create FolderID=" + FolderIDAdd);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Folders (FolderID, FolderName) VALUES (?, ?)");

            Scanner sc = new Scanner(System.in);

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
