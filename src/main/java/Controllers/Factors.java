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
            System.out.println("1");
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM Factors");
            System.out.println("2");
            ResultSet results = ps.executeQuery();
            System.out.println("3");
            System.out.println("FactorID | FactorName | FactorEffect");
            System.out.println("4");
            while (results.next()){
                System.out.println("5");
                int FactorID = results.getInt(1);
                System.out.println("6");
                String FactorName = results.getString(2);
                System.out.println("7");
                String FactorEffect = results.getString(3);
                System.out.println("8");
                System.out.println(FactorID + " | " + FactorName + " | " + FactorEffect);
                System.out.println("9");
            }
            //return read.toString();
            System.out.println("10");
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
