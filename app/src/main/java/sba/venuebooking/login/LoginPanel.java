package sba.venuebooking.login;

import sba.venuebooking.MainFrame;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel
{
    public LoginPanel()
    {
        setPreferredSize(MainFrame.SIZE);
        setBackground(new Color(0x909090));
        setLayout(null);

        JLabel title = new JLabel();
        title.setText("Login");
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Constantia", Font.PLAIN, 60));
        title.setBounds((MainFrame.SIZE.width - 200) / 2, 150, 200, 80);
        add(title);

        JLabel emailLabel = new JLabel();
        emailLabel.setText("Email:");
        emailLabel.setForeground(Color.BLACK);
        emailLabel.setFont(new Font("Constantia", Font.PLAIN, 26));
        emailLabel.setBounds(450, 345, 200, 50);
        add(emailLabel);

        JTextField email = new JTextField();
        email.setBounds(600, 350, 200, 30);
        email.setFont(new Font(null, Font.PLAIN, 14));
        email.setBackground(new Color(0xABABAB));
        email.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        email.setCaretColor(Color.BLACK);
        email.setForeground(Color.BLACK);
        add(email);

        JLabel passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font("Constantia", Font.PLAIN, 26));
        passwordLabel.setBounds(450, 405, 200, 50);
        add(passwordLabel);

        JPasswordField password = new JPasswordField();
        password.setBounds(600, 410, 200, 30);
        password.setFont(new Font("Consolas", Font.PLAIN, 15));
        password.setBackground(new Color(0xABABAB));
        password.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        password.setCaretColor(Color.BLACK);
        password.setForeground(Color.BLACK);
        add(password);

        LoginButton button = new LoginButton(email, password);
        button.setBounds((MainFrame.SIZE.width - 200) / 2, 500, 200, 50);
        button.setFont(new Font("Constantia", Font.PLAIN, 25));
        button.setBackground(new Color(0x828282));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setForeground(Color.BLACK);
        add(button);
    }
}
