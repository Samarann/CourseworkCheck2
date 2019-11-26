package Controllers;

import Server.Main;
import org.json.simple.JSONArray;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Folders {
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

    public static void deleteFolders(int FolderID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Folders WHERE FolderID = ?");
            ps.setInt(1, FolderID);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
