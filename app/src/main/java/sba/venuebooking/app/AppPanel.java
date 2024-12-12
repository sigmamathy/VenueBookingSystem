package sba.venuebooking.app;

import sba.venuebooking.Application;
import sba.venuebooking.MainFrame;
import javax.swing.*;
import java.awt.*;

public class AppPanel extends JPanel
{
    private final FunctionPanel[] functionPanels;
    private final SidePanel sidePanel;
    private FunctionPanel currentPanel;

    class SidePanel extends JPanel
    {
        JButton[] buttons;
        SidePanel() {
            setBounds(0, 0, 200, MainFrame.SIZE.height);
            setBackground(new Color(0xA0A0A0));
            setLayout(null);

            buttons = new JButton[5];
            Font font = new Font("Constantia", Font.PLAIN, 25);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i] = new JButton();
                buttons[i].setFont(font);
                buttons[i].setBackground(new Color(0x8B8B8B));
                buttons[i].setForeground(Color.BLACK);
                buttons[i].setBounds(10, 10 + i * 110, 180, 100);
                buttons[i].setFocusable(false);
                int index = i;
                buttons[i].addActionListener(e -> switchPanel(functionPanels[index]));
            }

            buttons[0].setText("Profile");
            buttons[1].setText("Book Now");
            buttons[2].setText("Schedule");
            buttons[3].setText("Request");
            buttons[4].setText("Admin");

            for (JButton button : buttons)
                add(button);

            JButton logoutButton = new JButton("Logout");
            logoutButton.setBackground(new Color(0x8B8B8B));
            logoutButton.setBounds(40, MainFrame.SIZE.height - 80, 120, 50);
            logoutButton.setFont(font);
            logoutButton.setFocusable(false);
            logoutButton.addActionListener(e -> Application.logout());
            add(logoutButton);
        }
        void onBegin() {
            buttons[3].setVisible(Application.userAuthorizationLevel > 1);
            buttons[4].setVisible(Application.userAuthorizationLevel > 2);
            switchPanel(functionPanels[0]);
            repaint();
        }
        void switchPanel(FunctionPanel panel)
        {
            if (currentPanel != panel)
            {
                currentPanel.setVisible(false);
                currentPanel = panel;
                panel.setVisible(true);
            }
            panel.onRefresh();
            AppPanel.this.repaint();
        }
    }

    public AppPanel()
    {
        setPreferredSize(MainFrame.SIZE);
        setLayout(null);

        sidePanel = new SidePanel();
        add(sidePanel);

        functionPanels = new FunctionPanel[5];
        functionPanels[0] = new ProfilePanel();
        functionPanels[1] = new BookNowPanel();
        functionPanels[2] = new SchedulePanel();
        functionPanels[3] = new RequestPanel();
        functionPanels[4] = new AdminPanel();

        currentPanel = functionPanels[0];
        for (int i = 0; i < functionPanels.length; i++) {
            functionPanels[i].setVisible(i == 0);
            add(functionPanels[i]);
        }
    }

    public void OnBegin()
    {
        sidePanel.onBegin();
        for (FunctionPanel panel: functionPanels)
            panel.onBegin();
    }
}
