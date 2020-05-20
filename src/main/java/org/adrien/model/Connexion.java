package org.adrien.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    public static Connection getConnexion() {

        String url = "jdbc:mysql://localhost/JavaFX_CRUD";
        String login = "root";
        String password = "";
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,login,password);

        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return conn;
    }

}
