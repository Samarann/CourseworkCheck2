package Controllers;

import Server.Main;
import org.json.simple.JSONArray;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SavesInside {
    public static void readSavesInside(){
        System.out.println("folders/read/");
        JSONArray read = new JSONArray();
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
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            //return "{\"error\": \"Unable to create user. Please see your server console for more information.\"}";
        }
    }
}
