package org.salonmaster.salonmaster.Connectionprovider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectionprovider {
    private static Connection con;

    public static Connection getConnection(){
      try{
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/trevita_ms";
            String username = "postgres";
            String password = "admin123";
            con = DriverManager.getConnection(url, username, password);
      }catch(ClassNotFoundException | SQLException e){
          System.out.println("Database connection error: " + e.getMessage());
      }
        return con;
    }
}
