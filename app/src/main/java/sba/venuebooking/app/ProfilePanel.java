package sba.venuebooking.app;

import sba.venuebooking.Application;
import sba.venuebooking.Database;
import sba.venuebooking.MainFrame;
import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends FunctionPanel
{
    RecentBookingPanel recentBookingPanel;

    ProfilePanel() {
        setBounds(200, 0, MainFrame.SIZE.width - 200, MainFrame.SIZE.height);
        setBackground(new Color(0xC6C6C6));
        setLayout(null);

        createSimpleLabel("Welcome", 80, 50, new Font("Constantia", Font.PLAIN, 50));
        createSimpleLabel("Your recent bookings:", 650, 80, new Font("Constantia", Font.PLAIN, 30));

        recentBookingPanel = new RecentBookingPanel(this);
        JScrollPane scrollPane = new JScrollPane(recentBookingPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(650, 150, 368, 500);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane);
    }

    @Override
    void onBegin() {
        recentBookingPanel.repaint();
    }

    @Override
    void onRefresh() {
        recentBookingPanel.repaint();
        repaint();
    }

    void onCancelBookingButtonPressed(int recordID)
    {
        if (!Database.containsBookingRecord(recordID))
        {
            JOptionPane.showMessageDialog(null,
                    "The required record does not exists.", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to cancel this booking record", "Confirm", JOptionPane.YES_NO_OPTION);

        if (result == 0) {
            Database.executeUpdate("DELETE FROM booking_records WHERE id = " + recordID);
            onRefresh();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(new Color(0xBCBCBC));
        g.fillRect(130, 140, 420, 180);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Constantia", Font.PLAIN, 25));
        g.drawString("Name: " + Application.userName, 150, 180);
        g.drawString("Email: " + Application.userEmail, 150, 230);
        String c = "";
        switch (Application.userAuthorizationLevel) {
            case 1 -> c = "Student";
            case 2 -> c = "Teacher";
            case 3 -> c = "Admin";
        }
        g.drawString("Classification: " + c, 150, 280);
    }
}
