package sba.venuebooking;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public class Application
{
    private static MainFrame frame;

    public static void main(String[] args) throws ClassNotFoundException
    {
        // initialize com.mysql.cj.jdbc.Driver class
        Class.forName("com.mysql.cj.jdbc.Driver");

        // delete expired records
        Database.executeUpdate("DELETE FROM booking_records WHERE date < '" + LocalDate.now() + "'");

        // creates a window frame and run it on a new thread
        frame = new MainFrame();
        frame.setVisible(true);

        //loginToApp("", "", "", 3);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String userEmail;
    public static String userName;
    public static String userPassword;
    public static int userAuthorizationLevel;

    public static void loginToApp(String email, String name, String password, int authorizationLevel)
    {
        userEmail = email;
        userName = name;
        userPassword = password;
        userAuthorizationLevel = authorizationLevel;

        frame.setContentPane(frame.appPanel);
        frame.appPanel.OnBegin();
        frame.pack();
    }

    public static void logout() {
        frame.setContentPane(frame.loginPanel);
        frame.pack();
    }
}
