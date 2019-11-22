package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.sqlite.SQLiteConfig;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Ownership {
    public static void readOwnership(){
        System.out.println("saves/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Ownership");
            ResultSet results = ps.executeQuery();
            System.out.println("UserID | SaveID");
            while (results.next()){
                int UserID = results.getInt(1);
                int SaveID = results.getInt(2);
                System.out.println(UserID + " | " + SaveID);
            }
            //return read.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}