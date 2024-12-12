package sba.venuebooking;

import sba.venuebooking.app.AppPanel;
import sba.venuebooking.login.LoginPanel;
import javax.swing.*;
import java.awt.*;

// Major application frame (window)
public class MainFrame extends JFrame
{
    // Size of the window (HD), constant
    public static final Dimension SIZE = new Dimension(1280, 720);

    // Child components of this class
    final LoginPanel loginPanel;
    final AppPanel appPanel;

    // Constructor, called when MainFrame was created.
    MainFrame() {
        setTitle("Venue Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginPanel = new LoginPanel();
        appPanel = new AppPanel();
        setContentPane(loginPanel);
        setSize(SIZE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }

}
