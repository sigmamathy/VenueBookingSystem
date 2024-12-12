package sba.venuebooking.app;

import sba.venuebooking.Application;
import sba.venuebooking.Database;
import sba.venuebooking.EmailSender;
import sba.venuebooking.MainFrame;
import javax.swing.*;
import java.awt.*;

public class RequestPanel extends FunctionPanel
{
    final RequestListPanel requestListPanel;
    private final JTextArea reasonArea;
    private final JButton acceptButton, declineButton;

    int currentSelectedRecord;

    RequestPanel() {
        setBounds(200, 0, MainFrame.SIZE.width - 200, MainFrame.SIZE.height);
        setBackground(new Color(0xC6C6C6));
        setLayout(null);

        createSimpleLabel("Student's Request", 80, 50, new Font("Constantia", Font.PLAIN, 50));

        requestListPanel = new RequestListPanel(this);
        currentSelectedRecord = -1;

        JScrollPane scrollPane = new JScrollPane(requestListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(80, 150, 268, MainFrame.SIZE.height - 200);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(scrollPane);

        reasonArea = new JTextArea();
        reasonArea.setBounds(550, 360, 400, 240);
        reasonArea.setEditable(false);
        reasonArea.setLineWrap(true);
        reasonArea.setVisible(false);
        reasonArea.setFont(new Font("Arial", Font.PLAIN, 20));
        reasonArea.setBackground(new Color(0xCBCBCB));
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
                reasonArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(reasonArea);

        acceptButton = new JButton("Accept");
        acceptButton.setBounds(920, 640, 100, 50);
        acceptButton.setBackground(new Color(0xB4B4B4));
        acceptButton.setForeground(Color.BLACK);
        acceptButton.setFont(new Font("Constantia", Font.PLAIN, 18));
        acceptButton.setFocusable(false);
        acceptButton.setVisible(false);

        declineButton = new JButton("Decline");
        declineButton.setBounds(800, 640, 100, 50);
        declineButton.setBackground(new Color(0xB4B4B4));
        declineButton.setForeground(Color.BLACK);
        declineButton.setFont(new Font("Constantia", Font.PLAIN, 18));
        declineButton.setFocusable(false);
        declineButton.setVisible(false);

        acceptButton.addActionListener(e ->
        {
            if (!Database.containsBookingRecord(currentSelectedRecord))
            {
                JOptionPane.showMessageDialog(null,
                        "The required record was declined or accepted by others.", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int result = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to accept this request?", "Confirm", JOptionPane.YES_NO_OPTION);
            // Sure make changes
            if (result == 0) {
                Database.executeQuery("SELECT * FROM booking_records WHERE id = " + currentSelectedRecord, set -> {
                    set.next();
                    EmailSender sender = new EmailSender();
                    sender.destination = set.getString("user");
                    sender.subject = "Your booking request have been accepted by " + Application.userName;
                    sender.text = "This is the booking that have been accepted:\n\nID: " + currentSelectedRecord
                            + "\nTitle: " + set.getString("title")
                            + "\nVenue: " + set.getString("venue")
                            + "\nDate: " + set.getDate("date").toString()
                            + "\nTime: " + set.getTime("start").toLocalTime().toString()
                            + " - " + set.getTime("end").toLocalTime().toString()
                            + "\nReason: " + set.getString("reason");
                    sender.send();
                });

                Database.executeUpdate("UPDATE booking_records SET confirmed = 1 WHERE id = " + currentSelectedRecord);
                reasonArea.setVisible(false);
                acceptButton.setVisible(false);
                declineButton.setVisible(false);
                currentSelectedRecord = -1;
                onRefresh();
            }
        });

        declineButton.addActionListener(e ->
        {
            if (!Database.containsBookingRecord(currentSelectedRecord))
            {
                JOptionPane.showMessageDialog(null,
                        "The required record was declined or accepted by others.", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String result = JOptionPane.showInputDialog(null,
                    "Give your reason for declining this request:", "Confirm", JOptionPane.QUESTION_MESSAGE);
            // Sure make changes
            if (result != null)
            {
                Database.executeQuery("SELECT * FROM booking_records WHERE id = " + currentSelectedRecord, set -> {
                    set.next();
                    EmailSender sender = new EmailSender();
                    sender.destination = set.getString("user");
                    sender.subject = "Your booking request have been declined by " + Application.userName;
                    sender.text = "This is the booking that have been declined:\n\nID: " + currentSelectedRecord
                            + "\nTitle: " + set.getString("title")
                            + "\nVenue: " + set.getString("venue")
                            + "\nDate: " + set.getDate("date").toString()
                            + "\nTime: " + set.getTime("start").toLocalTime().toString()
                            + " - " + set.getTime("end").toLocalTime().toString()
                            + "\nReason: " + set.getString("reason")
                            + "\n\nDeclined reason: " + result;
                    sender.send();
                });

                Database.executeUpdate("DELETE FROM booking_records WHERE id = " + currentSelectedRecord);
                currentSelectedRecord = -1;
                reasonArea.setVisible(false);
                acceptButton.setVisible(false);
                declineButton.setVisible(false);
                onRefresh();
            }
        });

        add(acceptButton);
        add(declineButton);
    }

    @Override
    void onBegin() {
        currentSelectedRecord = -1;
        onRefresh();
    }

    @Override
    void onRefresh() {
        requestListPanel.repaint();
        repaint();
    }

    void OnRequestSelected(int selectedRecord) {
        currentSelectedRecord = selectedRecord;
        reasonArea.setVisible(true);
        Database.executeQuery("SELECT * FROM booking_records WHERE id = " + currentSelectedRecord, set -> {
            set.next();
            reasonArea.setText(set.getString("reason"));
        });
        acceptButton.setVisible(true);
        declineButton.setVisible(true);
        onRefresh();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (currentSelectedRecord == -1) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
            g.drawString("Nothing is here.", 550, 350);
            return;
        }
        Database.executeQuery("SELECT * FROM booking_records WHERE id = " + currentSelectedRecord, set -> {
            set.next();
            g.setFont(new Font("Times New Roman", Font.BOLD, 40));
            g.setColor(Color.BLACK);
            g.drawString("Host: " + set.getString("user"), 450, 200);
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 25));
            g.drawString("Venue: " + set.getString("venue"), 450, 260);
            g.drawString("Date: " + set.getDate("date").toString(), 450, 300);
            g.drawString("Time: " + set.getTime("start").toLocalTime().toString()
                    + " - " + set.getTime("end").toLocalTime().toString(), 450, 340);
            g.drawString("Reason: ", 450, 380);
        });
    }
}
