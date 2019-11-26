package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.sqlite.SQLiteConfig;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Factors {
    public static void readFactors(){
        System.out.println("factors/read/");
        JSONArray read = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Factors");
            ResultSet results = ps.executeQuery();
            System.out.println("FactorID | FactorName | FactorEffect");
            while (results.next()){
                int FactorID = results.getInt(1);
                String FactorName = results.getString(2);
                String FactorEffect = results.getString(3);
                System.out.println(FactorID + " | " + FactorName + " | " + FactorEffect);
            }
            //return read.toString();
        }catch (Exception e){
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void deleteFactors(int FactorID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Factors WHERE FactorID = ?");
            ps.setInt(1, FactorID);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }

}
