package sba.venuebooking.login;

import sba.venuebooking.Application;
import sba.venuebooking.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Login "Confirm" button
public class LoginButton extends JButton implements ActionListener
{
    // Access login input fields
    JTextField emailInput;
    JPasswordField passwordInput;

    // Constructor
    LoginButton(JTextField email, JPasswordField password) {
        this.emailInput = email;
        this.passwordInput = password;
        setText("Continue");
        setFocusable(false);
        addActionListener(this);
    }

    // When the "Confirm" button is pressed by the user
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String query = "SELECT * FROM accounts WHERE email = '" + emailInput.getText() + "'";
        Database.executeQuery(query, set ->
        {
            String password = Application.hashPassword(String.valueOf(passwordInput.getPassword()));
            if (set.next() && set.getString("password").equals(password))
            {
                Application.loginToApp(
                        emailInput.getText(),
                        set.getString("name"),
                        password,
                        set.getInt("authorization_level")
                );
                emailInput.setText("");
                passwordInput.setText("");
            }
            else { // no results found
                JOptionPane.showMessageDialog(null,
                        "Invalid username or password.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
