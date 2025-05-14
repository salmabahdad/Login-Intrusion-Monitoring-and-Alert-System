package services;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailAlertService {
    public static void send(String toEmail, String subject, String messageText) {
        final String fromEmail = "youremail@gmail.com";
        final String password = "yourapppassword";


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(messageText);
            Transport.send(msg);
            System.out.println("Alert email sent!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
