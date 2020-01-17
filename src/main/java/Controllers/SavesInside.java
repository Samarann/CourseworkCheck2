package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("folders/")
public class SavesInside {
    @GET
    @Path("inside/")
    @Produces(MediaType.APPLICATION_JSON)
    public static void readSavesInside(){
        System.out.println("folders/inside/");
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM SavesInside");
            ResultSet results = ps.executeQuery();
            System.out.println("FolderID | SaveID");
            while (results.next()){
                int FolderID = results.getInt(1);
                int SaveID = results.getInt(2);
                System.out.println(FolderID + " | " + SaveID);
            }
            //return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            //return "{\"error\": \"Unable to create user. Please see your server console for more information.\"}";
        }
    }

    @POST
    @Path("insert/")
    @Produces(MediaType.APPLICATION_JSON)
    public static String insertSaveInside(@FormDataParam("FolderID") Integer FolderID, @FormDataParam("SaveID") Integer SaveID, @CookieParam("token") String token){
        System.out.println("folders/insert/");
        if(!Users.validToken(token)){
            return "{\"error\": \"You are not logged in.\"}";
        }
        try {
            if (FolderID == null || SaveID == null) {
                throw new Exception("One or more data parameters are missing values in the HTTP request.");
            }
            System.out.println("folders/insert FolderID=" + FolderID);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO SavesInside (FolderID, SaveID) VALUES (?, ?)");

            ps.setInt(1, FolderID);
            ps.setInt(2, SaveID);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";
            //read.toString();

        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
            return "{\"error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }
}
