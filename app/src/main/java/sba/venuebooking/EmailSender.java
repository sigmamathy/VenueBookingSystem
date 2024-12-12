package sba.venuebooking;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public String destination;
    public String subject;
    public String text;

    public void send()
    {
        Thread t = new Thread(() -> {
            String from = "xxxxx@xxxxx.xxx"; // email sender

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", 587);

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, "xxxxxxxx"); // Email password authentication code
                }
            });

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
                message.setSubject(subject);
                message.setText(text);
                Transport.send(message);
            }
            catch (MessagingException mex) {
                mex.printStackTrace();
            }
        });

        t.start();
    }

}
