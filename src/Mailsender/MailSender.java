package Mailsender;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailSender {
    private String toAdress, subject, msgToSend;
    private final String username = "erik1ortenholm@gmail.com";
    private final String password = "kkou ubbs gviv gaij";

    //Konstruktorn används av GUIprogrammet för att använda argumenten som användaren skriver in
    public MailSender(String toAdress, String subject, String msgToSend) {
        this.toAdress = toAdress;
        this.subject = subject;
        this.msgToSend = msgToSend;
    }

    //Skicka mailet och bekräfta att det har skickats
    public void sendMail() {
        Properties properties = configureProperties();
        Session session = createMailSession(password, properties);
        try {
            Message msg = createEmailMessage(session);

            Transport.send(msg);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //Konfigurera epostinställningarna
    private Properties configureProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }

    //Skapa en session med mitt användarnamn, lösenord och epostinställningarna
    private Session createMailSession(String password, Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("erik1ortenholm@gmail.com", password);
            }
        });
    }

    //Skapa mailet, med argumenten som skickades med i konstruktorn från anvädarens input i GUI
    private Message createEmailMessage(Session session) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAdress));
        msg.setSubject(subject);
        msg.setText(msgToSend);
        return msg;
    }
}
