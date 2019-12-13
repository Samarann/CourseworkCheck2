package Controllers;

import Server.Main;
import org.json.simple.JSONArray;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("folders/")
public class UserArea {
    @GET
    @Path("readall/")
    @Produces(MediaType.APPLICATION_JSON)
    public static void readUserArea(){
        System.out.println("folders/readall/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM UserArea");
            ResultSet results = ps.executeQuery();
            while (results.next()){
                int UserID = results.getInt(1);
                int FolderID = results.getInt(2);
                System.out.println(UserID + " | " + FolderID);
            }
            //return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
            //return "{\"error\"; \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
