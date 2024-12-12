package sba.venuebooking.app;

import sba.venuebooking.Application;
import sba.venuebooking.Database;
import sba.venuebooking.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdminPanel extends FunctionPanel
{
    AdminPanel() {
        setBounds(200, 0, MainFrame.SIZE.width - 200, MainFrame.SIZE.height);
        setBackground(new Color(0xC6C6C6));
        setLayout(null);

        createSimpleLabel("Email:", 80, 80);
        createSimpleLabel("Name:", 80, 130);
        createSimpleLabel("Password:", 80, 180);

        JTextField email = new JTextField();
        email.setBounds(160, 80, 250, 30);
        email.setFont(new Font(null, Font.PLAIN, 16));
        email.setBackground(new Color(0xDCDCDC));
        email.setBorder(BorderFactory.createCompoundBorder(
                email.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(email);

        JTextField name = new JTextField();
        name.setBounds(160, 130, 250, 30);
        name.setFont(new Font(null, Font.PLAIN, 16));
        name.setBackground(new Color(0xDCDCDC));
        name.setBorder(BorderFactory.createCompoundBorder(
                name.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(name);

        JPasswordField password = new JPasswordField();
        password.setBounds(200, 180, 210, 30);
        password.setFont(new Font("Consolas", Font.PLAIN, 16));
        password.setBackground(new Color(0xDCDCDC));
        password.setBorder(BorderFactory.createCompoundBorder(
                password.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(password);

        String[] choice = { "Student", "Teacher", "Admin" };
        JComboBox<String> classification = new JComboBox<>(choice);
        classification.setBounds(200, 230, 100, 30);
        classification.setFont(new Font(null, Font.PLAIN, 16));
        add(classification);

        JButton addUser = new JButton("Add user");
        addUser.setFocusable(false);
        addUser.setBounds(310, 230, 100, 30);
        addUser.setFont(new Font("Constantia", Font.PLAIN, 15));
        addUser.setBackground(new Color(0xB2B2B2));
        addUser.addActionListener(e ->
        {
            AtomicBoolean exists = new AtomicBoolean();
            Database.executeQuery("SELECT * FROM accounts WHERE email = '" + email.getText() + "'", set -> exists.set(set.next()));
            if (exists.get()) {
                JOptionPane.showMessageDialog(null,
                        "The required account already exists.", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pass = Application.hashPassword(String.valueOf(password.getPassword()));
            int level = 0;
            switch (Objects.requireNonNull(classification.getSelectedItem()).toString()) {
                case "Student" -> level = 1;
                case "Teacher" -> level = 2;
                case "Admin" -> level = 3;
            }
            String query = "INSERT INTO accounts VALUES ('" + email.getText() + "', '"
                    + name.getText() + "', '" + pass + "', " + level + ")";
            Database.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Succeed.");
        });

        add(addUser);

        createSimpleLabel("Email:", 80, 400);
        JTextField email2 = new JTextField();
        email2.setBounds(160, 400, 250, 30);
        email2.setFont(new Font(null, Font.PLAIN, 16));
        email2.setBackground(new Color(0xDCDCDC));
        email2.setBorder(BorderFactory.createCompoundBorder(
                email2.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        add(email2);

        JButton removeUser = new JButton("Remove user");
        removeUser.setFocusable(false);
        removeUser.setBounds(280, 450, 130, 30);
        removeUser.setFont(new Font("Constantia", Font.PLAIN, 15));
        removeUser.setBackground(new Color(0xB2B2B2));
        removeUser.addActionListener(e ->
        {
            AtomicBoolean exists = new AtomicBoolean();
            Database.executeQuery("SELECT * FROM accounts WHERE email = '" + email2.getText() + "'", set -> exists.set(set.next()));
            if (!exists.get()) {
                JOptionPane.showMessageDialog(null,
                        "The required account does not exists.", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String query = "DELETE FROM accounts WHERE email = '" + email2.getText() + "'";
            Database.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Succeed.");
        });
        add(removeUser);
    }

    @Override
    void onBegin() {

    }

    @Override
    void onRefresh() {
        repaint();
    }
}
