package Mailsender;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

public class MailReceiver {
    private String receiveHost = "imap.gmail.com"; // Replace with the IMAP server of your email provider
    private String receiveUsername = "erik1ortenholm@gmail.com"; // Your email username
    private String receivePassword = "kkou ubbs gviv gaij"; // Your email password


    /*ReceiveMail metoden ska ständigt skriva ut nya meddelanden i MailAppSwingGui textarean till höger under rubriken inbox
    Implemmentera en "refresh button" som användaren kan trycka på för att se ifall det kommit in några nya mail i GUI:n
    Kanske göra en lista? Och låta användaren välja ett mail ifrån subject, och sen visa hela mailet/meddelandet
    Jlist?*/

    public void receiveMail() throws MessagingException, IOException {
        Properties properties = configureProperties();
        Session session = Session.getInstance(properties, null);
        Store store = session.getStore("imap");
        store.connect(receiveHost, receiveUsername, receivePassword);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        for (Message message : messages) {
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Date: " + message.getSentDate());
            System.out.println("Message: " + message.getContent());
            System.out.println("-----------------------------");
        }

        inbox.close(false);
        store.close();
    }

    //Konfigurera epostinställningarna
    private Properties configureProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.starttls.enable", "true"); // Enable TLS
        return properties;
    }
}
