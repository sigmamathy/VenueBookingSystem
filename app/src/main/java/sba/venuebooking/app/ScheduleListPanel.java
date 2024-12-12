package sba.venuebooking.app;

import sba.venuebooking.Database;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleListPanel extends JPanel
{
    ScheduleListPanel() {
        setLayout(null);
        setBackground(new Color(0xD8D8D8));

        AtomicInteger yLocation = new AtomicInteger(0);
        for (int j = 0; j < 7; j++)
        {
            LocalDate date = LocalDate.now().plusDays(j);
            String query = "SELECT * FROM booking_records WHERE date = '" + date + "'";
            Database.executeQuery(query, set ->
            {
                if (!set.next()) return;
                int yLoc = yLocation.get();
                yLoc += 45;
                do yLoc += 50;
                while (set.next());
                yLocation.set(yLoc);
            });
        }
        setPreferredSize(new Dimension(800, Math.max(500, yLocation.get())));
    }

    private record BookRecordDisplay(String duration, String title, String remark) {}

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        AtomicInteger yLocation = new AtomicInteger(0);
        for (int j = 0; j < 7; j++)
        {
            LocalDate date = LocalDate.now().plusDays(j);
            String query = "SELECT * FROM booking_records WHERE date = '" + date + "'";
            Database.executeQuery(query, set ->
            {
                if (!set.next()) return;
                int yLoc = yLocation.get();
                g.setColor(new Color(0xC2C2C2));
                g.fillRect(0, yLoc, 800, 45);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString(date.toString(), 350, yLoc + 30);
                g.drawRect(0, yLoc, 800, 45);
                yLoc += 45;

                HashMap<String, ArrayList<BookRecordDisplay>> maps = new HashMap<>();
                do {
                    String venue = set.getString("venue");
                    if (!maps.containsKey(venue))
                        maps.put(venue, new ArrayList<>());
                    String duration = set.getTime("start").toLocalTime().toString()
                            + " - " + set.getTime("end").toLocalTime().toString();
                    String remark = Database.getNameByEmail(set.getString("user"));
                    if (!set.getBoolean("confirmed"))
                        remark += " (Pending)";
                    maps.get(venue).add(new BookRecordDisplay(duration, set.getString("Title"), remark));
                } while (set.next());
                maps.forEach((key, value) -> value.sort(Comparator.comparing(x -> x.duration)));

                g.setFont(new Font("Arial", Font.PLAIN, 15));
                for (var pair : maps.entrySet()) {
                    int ySize = pair.getValue().size() * 50;
                    g.setColor(Color.BLACK);
                    g.drawRect(0, yLoc, 150, ySize);
                    g.drawString(pair.getKey(), 10, yLoc + ySize / 2 + 5);
                    for (int i = 0; i < pair.getValue().size(); i++) {
                        g.setColor(Color.BLACK);
                        g.drawRect(150, yLoc + i * 50, 650, 50);
                        g.drawString(pair.getValue().get(i).duration, 180, yLoc + i * 50 + 30);
                        g.drawLine(300, yLoc + i * 50, 300, yLoc + i * 50 + 50);
                        g.drawString(pair.getValue().get(i).title, 320, yLoc + i * 50 + 30);
                        g.setColor(Color.GRAY);
                        g.drawString(pair.getValue().get(i).remark, 600, yLoc + i * 50 + 30);
                    }
                    yLoc += ySize;
                }
                yLocation.set(yLoc);
            });
        }
        setPreferredSize(new Dimension(800, Math.max(500, yLocation.get())));
    }
}
