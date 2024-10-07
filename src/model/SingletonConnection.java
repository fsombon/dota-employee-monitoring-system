package model;

import java.sql.*;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;

public class SingletonConnection {

    private final String SERVER = "localhost:3306";
    private final String DB = "monitoringsystem";
    private final String DB_USER_NAME = "root";
    private final String DB_PASSWORD = "";
    private final int MAX_CONNECTIONS = 300;
    private static SingletonConnection instance = null;
    private Connection con;
    
    private SingletonConnection(){
        
    }

    public static SingletonConnection getInstance(){
        if (instance == null){
            instance = new SingletonConnection();   
        }
        return instance;
    }
    
    private String getSERVER() {
        return SERVER;
    }

    private String getDB() {
        return DB;
    }

    private String getUSER() {
        return DB_USER_NAME;
    }

    private String getPASS() {
        return DB_PASSWORD;
    }
    
    public void connectDatabase() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        //con = java.sql.DriverManager.getConnection("jdbc:mysql://" + getSERVER() + "/" + getDB(), getUSER(), getPASS());
        String url = "jdbc:mysql://" + getSERVER() + "/" + getDB() + "?max_connections=" + MAX_CONNECTIONS;
        con = java.sql.DriverManager.getConnection(url, getUSER(), getPASS());
        
    }
    
    public void reconnect() throws Exception {
        con = java.sql.DriverManager.getConnection("jdbc:mysql://" + getSERVER() + "/" + getDB(), getUSER(), getPASS());
    }
    
    public Connection openConnection(){
        return con;
    }
    
    public void closeConnection() throws SQLException{
        if (con != null){
            con.close();
        }
    }
}
