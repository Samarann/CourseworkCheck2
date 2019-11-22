package Controllers;

import Server.Main;
import org.json.simple.JSONArray;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserArea {
    public static void readUserArea(){
        System.out.println("folders/read/");
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
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
