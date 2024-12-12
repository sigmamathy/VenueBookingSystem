package sba.venuebooking;

import java.sql.*;

// Load all tables from the database
public class Database
{
    private static final String url = "jdbc:mysql://localhost:3307/ihmc_venuebooking";

    @FunctionalInterface
    public interface SQLResultSetHandler {
        void actionPerformed(ResultSet set) throws SQLException;
    }

    public static void executeQuery(String query, SQLResultSetHandler handler) {
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement statement = connection.createStatement();
            var set = statement.executeQuery(query);

            handler.actionPerformed(set);
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeUpdate(String query) {
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean containsBookingRecord(int recordID) {
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement statement = connection.createStatement();
            var set = statement.executeQuery("SELECT * FROM booking_records WHERE id = " + recordID);
            boolean result = set.next();
            connection.close();
            return result;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getNameByEmail(String email) {
        try {
            Connection connection = DriverManager.getConnection(url, "root", "");
            Statement statement = connection.createStatement();
            var set = statement.executeQuery("SELECT * FROM accounts WHERE email = '" + email + "'");
            set.next();
            String name = set.getString("name");
            connection.close();
            return name;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
