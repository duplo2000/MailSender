package Mailsender;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

/**
 * Klassen MailSender ansvarar för att skicka e-postmeddelanden med hjälp av SMTP.
 * Den möjliggör för användaren att ange mottagarens e-postadress, ämne och meddelandeinnehåll och
 * skickar e-postmeddelandet med hjälp av de tillhandahållna SMTP-serverinställningarna och användaruppgifterna.
 * <p>
 * Denna klass kapslar in funktionaliteten för att skapa och skicka e-postmeddelanden, inklusive konfigurationen
 * av SMTP-inställningar och e-postinnehållet.
 */

public class MailSender {
    private String toAddress, subject, msgToSend;
    private String username;
    private String password;
    private File fileToSend;


    /**
     * Konstruktor som använder användarens gmail-address och specifika lösenord.
     *
     * @param toAddress Email-adressen till mottagaren.
     * @param subject   Mailets ämne.
     * @param msgToSend Själva meddelandet.
     * @param username  Användarens gmail.
     * @param password  Användarens lösenord.
     */
    public MailSender(String toAddress, String subject, String msgToSend, String username, String password) {
        this.toAddress = toAddress;
        this.subject = subject;
        this.msgToSend = msgToSend;
        this.username = username;
        this.password = password;
    }

    /**
     * Skickar mailet genom att använda de angivna SMTP inställningarna och användarens uppgifter
     */
    public void sendMail() {
        Properties properties = configureProperties();
        Session session = createMailSession(password, properties);
        try {
            Message msg = createEmailMessage(session);
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Konfigurerar e-postegenskaper, inklusive SMTP, port- och autentiseringsinställningar.
     *
     * @return Propertiesobjekt som innehåller e-postkonfigurationen.
     */
    private Properties configureProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }

    /**
     * Skapar en e-postsession med angivna autentiseringsuppgifterna och epostkonfigurationen.
     *
     * @param password   Special-lösenordet till mailkontot.
     * @param properties Propertiesobjekt som innehåller konfigurationen.
     * @return Ett Session-objekt för att skicka meddelanden.
     */
    private Session createMailSession(String password, Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Skapar mailet genom att använda mottagarens mail, ämne samt själva meddelandet.
     *
     * @param session Den aktuella Sessionen.
     * @return Ett MimeMessage som representerar meddelandet som ska skickas.
     * @throws MessagingException om det blir ett problem att skicka mailet.
     */
    private Message createEmailMessage(Session session) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        msg.setSubject(subject);

        Multipart multipart = new MimeMultipart();

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(msgToSend);
        multipart.addBodyPart(textPart);

        if (fileToSend != null) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            try {
                attachmentPart.attachFile(fileToSend);
                multipart.addBodyPart(attachmentPart);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        msg.setContent(multipart);

        return msg;
    }

    /**
     * Sätter den bilaga som användaren vill skicka med i e-postmeddelandet.
     *
     * @param attachment Bilagan användaren vill skicka.
     */
    public void setAttachment(File attachment) {
        fileToSend = attachment;
    }
}
