package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.sqlite.SQLiteConfig;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class SaveData {
    public static void readSaveData(){
        System.out.println("saves/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM SaveData");
            ResultSet results = ps.executeQuery();
            System.out.println("SaveID | FactorID");
            while (results.next()){
                int SaveID = results.getInt(1);
                int FactorID = results.getInt(2);
                System.out.println(SaveID + " | " + FactorID);
            }
            //return read.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}