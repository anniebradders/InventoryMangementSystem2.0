package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//method to set up the database connection
public class databaseConnection {

    public static Connection connect() {
        Connection conn = null;
        //gets the filename of where the database is held
        String fileName = "ticketDatabase.db";
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName;
        try {
            //creates the connection
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //returns the databaseConnection
        return conn;
    }
}

