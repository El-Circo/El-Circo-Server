package github.turtlehunter.ElCircoServer;

import java.sql.*;

public class DatabaseManager {
    public String errorMsg;
    private Connection connection;

    public DatabaseManager() {
        try {
            Class.forName("com.msql.jdbc.Driver");
        } catch (Exception e) {
            Main.instance.loggingManager.write("Cant connect to database, No driver");
        }
        if (!connect()) {
            Main.instance.loggingManager.write("Cant connect to database");
        }
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/elcirco", "urielsalis", "password");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet runSql(String sql) {
        Statement s = null;
        try {
            s = connection.createStatement();
            return s.executeQuery (sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
