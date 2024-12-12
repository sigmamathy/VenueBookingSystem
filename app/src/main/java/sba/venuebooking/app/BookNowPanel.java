package sba.venuebooking.app;

import sba.venuebooking.Application;
import sba.venuebooking.Database;
import sba.venuebooking.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookNowPanel extends FunctionPanel
{
    // Data input by user
    private final JTextField title;
    private final JComboBox<String> venues, dates;
    private final JComboBox<String> fromHour, fromMinute;
    private final JComboBox<String> toHour, toMinute;
    private final JTextArea reason;

    BookNowPanel()
    {
        setBounds(200, 0, MainFrame.SIZE.width - 200, MainFrame.SIZE.height);
        setBackground(new Color(0xC6C6C6));
        setLayout(null);

        createSimpleLabel("Book Now", 80, 50, new Font("Constantia", Font.PLAIN, 50));
        createSimpleLabel("Title:", 350, 60, new Font("Constantia", Font.PLAIN, 30));
        createSimpleLabel("Select Venue:", 80, 150);
        createSimpleLabel("Select Date:", 500, 150);
        createSimpleLabel("From          :", 80, 400);
        createSimpleLabel("To          :", 310, 400);
        createSimpleLabel("Reason:", 80, 450);

        title = new JTextField();
        title.setBounds(430, 55, 400, 40);
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        title.setBorder(BorderFactory.createCompoundBorder(
                title.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        title.setBackground(new Color(0xDCDCDC));
        add(title);

        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        String[] dateList = new String[14];
        for (int i = 0; i < dateList.length; i++)
            dateList[i] = today.plusDays(i + 1).toString();

        String[] venueList = {
                "Room 100",
                "Room 101",
                "Room 102",
                "Room 103",
                "Room 104",
                "Room 105",
                "Room 106",
                "Room 107",
                "Room 108",
                "Room 109",
                "Room 110",
                "Room 200",
                "Room 201",
                "Room 202"
        };
        
        venues = new JComboBox<>(venueList);
        venues.setBounds(250, 150, 150, 30);
        venues.setFont(new Font(null, Font.PLAIN, 15));
        
        dates = new JComboBox<>(dateList);
        dates.setBounds(650, 150, 150, 30);
        dates.setFont(new Font(null, Font.PLAIN, 15));
        add(dates);
        
        venues.addActionListener(e -> onRefresh());
        dates.addActionListener(e -> onRefresh());
        add(venues);
        add(dates);

        String[] hourList = {"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18"};
        String[] minuteList = new String[60];
        for (int i = 0; i < 60; i++)
            minuteList[i] = i < 10 ? "0" + i : String.valueOf(i);

        fromHour = createSimpleComboBox(hourList, 150, 400);
        fromMinute = createSimpleComboBox(minuteList, 220, 400);
        toHour = createSimpleComboBox(hourList, 350, 400);
        toMinute = createSimpleComboBox(minuteList, 420, 400);

        reason = new JTextArea();
        reason.setBounds(180, 458, 400, 200);
        reason.setFont(new Font("Arial", Font.PLAIN, 18));
        reason.setLineWrap(true);
        reason.setBackground(new Color(0xDCDCDC));
        reason.setBorder(BorderFactory.createCompoundBorder(
            reason.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(reason);

        JButton submit = new JButton("Submit");
        submit.setFont(new Font("Constantia", Font.PLAIN, 20));
        submit.setBounds(600, 608, 100, 50);
        submit.setBackground(new Color(0xB2B2B2));
        submit.setFocusable(false);
        submit.addActionListener(this::OnSubmitButtonPressed);
        add(submit);
    }

    private JComboBox<String> createSimpleComboBox(String[] items, int x, int y)
    {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBounds(x, y, 50, 30);
        comboBox.setFont(new Font(null, Font.PLAIN, 15));
        add(comboBox);
        return comboBox;
    }

    void OnSubmitButtonPressed(ActionEvent e)
    {
        if (title.getText().isEmpty() || title.getText().length() > 40) {
            JOptionPane.showMessageDialog(null,
                    "Please give a title to your booking. (within 40 characters)", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reason.getText().length() > 500) {
            JOptionPane.showMessageDialog(null,
                    "Reason should not more than 500 characters.", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalTime start = LocalTime.parse(fromHour.getSelectedItem() + ":" + fromMinute.getSelectedItem() + ":00");
        LocalTime end = LocalTime.parse(toHour.getSelectedItem() + ":" + toMinute.getSelectedItem() + ":00");

        if (!end.isAfter(start) || end.isAfter(LocalTime.of(18, 0))) {
            JOptionPane.showMessageDialog(null,
                    "The time given is not available.", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT start, end, confirmed FROM booking_records WHERE venue = '"
                + venues.getSelectedItem() + "' AND date = '" + dates.getSelectedItem() + "'";
        AtomicBoolean overlaps = new AtomicBoolean(false);
        Database.executeQuery(query, set -> {
            while (set.next()) {
                if (end.isAfter(set.getTime("start").toLocalTime()) && start.isBefore(set.getTime("end").toLocalTime())) {
                    overlaps.set(true);
                    break;
                }
            }
        });

        if (overlaps.get()) {
            JOptionPane.showMessageDialog(null,
                    "The time given is not available.", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean confirmed = sendConfirmMessage();

        if (!confirmed)
            return;

        int id;
        do {
            id = new Random().nextInt(100000000);
        } while (Database.containsBookingRecord(id));

        query = "INSERT INTO booking_records VALUES (" + id + ", '" + title.getText() + "', '"
                + venues.getSelectedItem() + "', '" + dates.getSelectedItem()
                + "', '" + start + ":00', '" + end + ":00', '" + Application.userEmail + "', '" + reason.getText() + "', "
                + (Application.userAuthorizationLevel > 1 ? "1" : "0") + ")";
        Database.executeUpdate(query);
        onRefresh();

        JOptionPane.showMessageDialog(null,
                "The required venue has already been booked.\nYou may go to \"Profile\" tab to see your record.",
                "Success!", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean sendConfirmMessage()
    {
        String msg = "Confirm your following record:\n\n";
        msg += "Title: " + title.getText() + '\n';
        msg += "Venue: " + venues.getSelectedItem() + '\n';
        msg += "Date: " + dates.getSelectedItem() + '\n';
        msg += "Start time: " + fromHour.getSelectedItem() + ':' + fromMinute.getSelectedItem() + '\n';
        msg += "End time: " + toHour.getSelectedItem() + ':' + toMinute.getSelectedItem() + '\n';
        msg += "User: " + Application.userName + '\n';
        msg += "Reason: " + reason.getText() + "\n\n";
        msg += "By clicking \"Yes\", you agreed to submit this venue booking record to our database.";
        if (Application.userAuthorizationLevel == 1) {
            msg += """
                    
                    
                    Note: As a student, you don't have permission to submit your record directly.
                    Your record will be marked as pending until accepted by a teacher or an admin.""";
        }
        int result = JOptionPane.showConfirmDialog(null,
                msg, "Venue Booking Record Submission", JOptionPane.YES_NO_OPTION);
        return result == 0;
    }

    @Override
    void onBegin() {
        onRefresh();
    }

    @Override
    void onRefresh() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        {
            ArrayList<int[]> xList = new ArrayList<>();
            String query = "SELECT * FROM booking_records WHERE venue = '"
                    + venues.getSelectedItem() + "' AND date = '" + dates.getSelectedItem() + "'";
            Database.executeQuery(query, set ->
            {
                while (set.next())
                {
                    LocalTime start = set.getTime("start").toLocalTime();
                    LocalTime end = set.getTime("end").toLocalTime();
                    boolean confirmed = set.getBoolean("confirmed");

                    int x1 = (int) (80 + (start.getHour() - 8 + start.getMinute() / 60.0f) * 90);
                    int x2 = (int) (80 + (end.getHour() - 8 + end.getMinute() / 60.0f) * 90);
                    g.setColor(new Color(confirmed ? 0xBE6F6F : 0xd1c675));
                    g.fillRect(x1, confirmed ? 310 : 285, x2 - x1, 30);
                    xList.add(new int[]{x1, x2});
                }
            });

            g.setColor(new Color(0x86B786));
            if (xList.isEmpty()) {
                g.fillRect(80, 260, 900, 30);
            }
            else {
                xList.sort((xs, xe) -> Integer.compare(xs[0], xe[1]));
                g.fillRect(80, 260, xList.get(0)[0] - 80, 30);
                for (int i = 1; i < xList.size(); i++)
                    g.fillRect(xList.get(i - 1)[1], 260, xList.get(i)[0] - xList.get(i - 1)[1], 30);
                g.fillRect(xList.get(xList.size() - 1)[1], 260, 980 - xList.get(xList.size() - 1)[1], 30);
            }
        }

        g.setColor(Color.BLACK);
        for (int i = 0; i < 21; i++)
        {
            if ((i & 1) == 1) {
                g.drawLine(80 + i * 45, 250, 80 + i * 45, 350);
                continue;
            }
            g.drawString((i / 2 + 8) + ":00", 80 + i * 45, 240);
            g.drawLine(80 + i * 45, 245, 80 + i * 45, 350);
            g.drawLine(80 + i * 45, 245, 100 + i * 45, 245);
        }

        g.setColor(new Color(0x86B786));
        g.fillRect(800, 380, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRect(800, 380, 20, 20);
        g.setFont(new Font("Constantia", Font.PLAIN, 25));
        g.drawString("Available", 830, 398);

        g.setColor(new Color(0xd1c675));
        g.fillRect(800, 420, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRect(800, 420, 20, 20);
        g.drawString("Pending", 830, 438);

        g.setColor(new Color(0xBE6F6F));
        g.fillRect(800, 460, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRect(800, 460, 20, 20);
        g.drawString("Unavailable", 830, 479);

    }
}
