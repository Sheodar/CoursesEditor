package  methods.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB {
    public static ResultSet res;
    public static Statement stmt;
    private static Connection conn;

    public static void DBConnect() throws SQLException {
        String urll = "jdbc:sqlite:CoursesEditorDB.db";
        conn = null;
        try {
            conn = DriverManager.getConnection(urll);
            System.out.println("DataBase <CONNECTION> is connected.");
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DBDisconnect() {
        try {
            if (conn != null) {
                System.out.println("DataBase <CONNECTION> is disconnected.");
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void ResDisconnect() {
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void StmtDisconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
