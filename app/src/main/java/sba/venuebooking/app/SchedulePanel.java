package sba.venuebooking.app;

import sba.venuebooking.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SchedulePanel extends FunctionPanel
{
    final ScheduleListPanel scheduleListPanel;

    SchedulePanel()
    {
        setBounds(200, 0, MainFrame.SIZE.width - 200, MainFrame.SIZE.height);
        setBackground(new Color(0xC6C6C6));
        setLayout(null);

        createSimpleLabel("Schedule", 80, 50, new Font("Constantia", Font.PLAIN, 50));

        scheduleListPanel = new ScheduleListPanel();
        JScrollPane scrollPane = new JScrollPane(scheduleListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(100, 150, 818, 500);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(scrollPane);
    }

    @Override
    void onBegin() {

    }

    @Override
    void onRefresh() {
        scheduleListPanel.repaint();
        repaint();
    }
}
