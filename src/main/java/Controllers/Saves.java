package Controllers;

import Server.Main;
import org.json.simple.JSONArray;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Saves {
    public static void readSaves(){
        System.out.println("saves/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Saves");
            ResultSet results = ps.executeQuery();
            System.out.println("SaveID | SaveName");
            while (results.next()){
                int SaveID = results.getInt(1);
                String SaveName = results.getString(2);
                System.out.println(SaveID + " | " + SaveName);
            }
            //return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void deleteSaves(int SaveID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Saves WHERE SaveID = ?");
            ps.setInt(1, SaveID);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
