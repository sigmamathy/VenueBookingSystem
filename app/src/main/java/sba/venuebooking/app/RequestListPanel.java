package sba.venuebooking.app;

import sba.venuebooking.Database;
import sba.venuebooking.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RequestListPanel extends JPanel implements MouseListener
{
    private final RequestPanel parent;

    RequestListPanel(RequestPanel parent) {
        this.parent = parent;
        setBackground(new Color(0xD8D8D8));
        setLayout(null);

        Database.executeQuery("SELECT * FROM booking_records WHERE confirmed = 0", set -> {
            int yLocation = 0;
            for (; set.next(); yLocation += 120);
            setPreferredSize(new Dimension(250, Math.max(yLocation, MainFrame.SIZE.height - 200)));
        });

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        String query = "SELECT * FROM booking_records WHERE confirmed = 0";
        Database.executeQuery(query, set -> {
            int yLocation = 0;
            for (; set.next(); yLocation += 120)
            {
                g.setColor(new Color((parent.currentSelectedRecord == set.getInt("id")) ? 0xACACAC : 0xBBBBBB));
                g.fillRoundRect(10, 10 + yLocation, 230, 100, 30, 30);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.BLACK);
                String title = set.getString("title");
                if (title.length() > 16)
                    title = title.substring(0, 14) + "...";
                g.drawString(title, 20, 45 + yLocation);
                g.setColor(Color.GRAY);
                g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.drawString(set.getDate("date").toString() + "   " + set.getString("venue"), 20, 90 + yLocation);

                g.setColor(new Color(0xFFF787));
                g.fillOval(210, 20 + yLocation, 20, 20);
            }
            setPreferredSize(new Dimension(250, Math.max(yLocation, MainFrame.SIZE.height - 200)));
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        String query = "SELECT * FROM booking_records WHERE confirmed = 0";
        Database.executeQuery(query, set -> {
            int yLocation = 0;
            for (; set.next(); yLocation += 120)
            {
                if (e.getX() >= 10 && e.getX() <= 240
                        && e.getY() >= yLocation + 10 && e.getY() <= yLocation + 110)
                {
                    parent.OnRequestSelected(set.getInt("id"));
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
