package common;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {

    private static MailService object;

    private final static String from = "ranjini.c@zohocorp.com";
    private final static String password = "FG5p5QQ167aH";

    private final static String host = "smtppro.zoho.com";
    private final static String port = "465";

    private final static String enableSSL = "true";

    private final Session session;

    private MailService() {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", enableSSL);
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }

        });
    }

    public static MailService getInstance() {
        // Create a new object, if object is null
        if (object == null) {
            object = new MailService();
        }

        // Return object
        return object;
    }

    public boolean sendText(String to, String subject, String text) {
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(text);

            System.out.println("Sending the text mail...");

            // Send message
            Transport.send(message);

            System.out.println("Mail sent successfully....");

            return true;
        } catch (MessagingException exception) {
            exception.printStackTrace();

            return false;
        }
    }

    public boolean sendHTML(String to, String subject, String html) {
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setContent(html, "text/html");

            System.out.println("Sending the HTML mail...");

            // Send message
            Transport.send(message);

            System.out.println("Mail sent successfully....");

            return true;
        } catch (MessagingException exception) {
            exception.printStackTrace();

            return false;
        }
    }
}