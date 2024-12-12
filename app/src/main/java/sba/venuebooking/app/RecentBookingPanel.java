package sba.venuebooking.app;

import sba.venuebooking.Application;
import sba.venuebooking.Database;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RecentBookingPanel extends JPanel implements MouseListener
{
    private final ProfilePanel parent;

    RecentBookingPanel(ProfilePanel parent)
    {
        this.parent = parent;
        setLayout(null);
        setBackground(new Color(0xD8D8D8));

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        String query = "SELECT * FROM booking_records WHERE user = '" + Application.userEmail + "'";
        Database.executeQuery(query, set ->
        {
            int yLocation = 0;
            for (; set.next(); yLocation += 150)
            {
                g.setColor(new Color(0xBBBBBB));
                g.fillRoundRect(10, 10 + yLocation, 330, 130, 30, 30);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.BLACK);
                String title = set.getString("title");
                if (title.length() > 18)
                    title = title.substring(0, 16) + "...";
                g.drawString(title, 20, 45 + yLocation);
                g.setColor(Color.GRAY);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString(set.getDate("date").toString() + "   " + set.getString("venue"), 20, 90 + yLocation);
                g.drawString(set.getTime("start").toLocalTime().toString() + " - "
                        + set.getTime("end").toLocalTime().toString(), 20, 120 + yLocation);

                g.setColor(new Color(set.getBoolean("confirmed") ? 0xB0FFA3 : 0xfffa91));
                g.fillRoundRect(230, 20 + yLocation, 100, 30, 30, 30);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.drawString(set.getBoolean("confirmed") ? "Confirmed" : "Pending",
                        set.getBoolean("confirmed") ? 242 : 250, 40 + yLocation);

                g.setColor(new Color(0xAAAAAA));
                g.fillRoundRect(290, 90 + yLocation, 40, 40, 20, 20);
                g.setFont(new Font("Consolas", Font.BOLD, 35));
                g.setColor(Color.DARK_GRAY);
                g.drawString("x", 300, 120 + yLocation);
            }
            setPreferredSize(new Dimension(350, Math.max(yLocation, 500)));
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Database.executeQuery("SELECT * FROM booking_records WHERE user = '" + Application.userEmail + "'", set ->
        {
            for (int yLocation = 0; set.next(); yLocation += 150)
            {
                if (e.getX() >= 290 && e.getX() <= 330
                        && e.getY() >= 90 + yLocation && e.getY() <= 130 + yLocation)
                {
                    parent.onCancelBookingButtonPressed(set.getInt("id"));
                    break;
                }
            }
        });
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
