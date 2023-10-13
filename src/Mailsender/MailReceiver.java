package Mailsender;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Klassen MailReceiver ansvarar för att ta emot e-postmeddelanden via IMAP.
 * Den ansluter till en e-postserver, hämtar e-postmeddelanden i INBOX-mappen,
 * och konverterar och returnerar meddelandeinnehållet som text.
 * <p>
 * Denna klass inkluderar metoder för att konfigurera e-postinställningar, ta emot e-postmeddelanden, extrahera
 * meddelandeinnehåll och konvertera det till text samt hantera det senast mottagna meddelandet.
 */
public class MailReceiver {
    private String receiveHost;
    private String receiveUsername;
    private String receivePassword;

    private Message mostRecentMessage;
    private String mostRecentMessageString;

    /**
     * Konstruktor med standardinställningar
     */
    public MailReceiver() {
        this("imap.gmail.com", "erik1ortenholm@gmail.com", "kkou ubbs gviv gaij");
    }

    /**
     * Konstruktor med användarens inställningar
     */

    public MailReceiver(String receiveHost, String receiveUsername, String receivePassword) {
        this.receiveHost = receiveHost;
        this.receiveUsername = receiveUsername;
        this.receivePassword = receivePassword;
    }

    /**
     * Tar emot e-postmeddelanden genom att ansluta till en IMAP-server och hämta meddelanden från INBOX-mappen.
     * Konverterar det senast mottagna meddelandet till en textrepresentation och sparar det.
     *
     * @throws MessagingException om ett fel uppstår under hantering av e-postmeddelanden.
     * @throws IOException        om det uppstår ett in- eller utdatafel.
     */
    public void receiveMail() throws MessagingException, IOException {
        Properties properties = configureProperties();
        Session session = Session.getInstance(properties, null);
        Store store = session.getStore("imaps");

        store.connect(receiveHost, receiveUsername, receivePassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        mostRecentMessage = messages[messages.length - 1];
        convertRecentMessageToString();

        inbox.close(false);
        store.close();
    }


    /**
     * Konfigurerar e-postinställningarna, inklusive protokoll och serverinformation för IMAP.
     *
     * @return Properties-objekt som innehåller inställningarna.
     */
    private Properties configureProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps"); // Use "imaps" for secure IMAP
        properties.setProperty("mail.imaps.host", receiveHost); // Set the IMAPS host
        properties.setProperty("mail.imaps.port", "993"); // Specify the IMAPS port
        properties.setProperty("mail.imaps.starttls.enable", "true"); // Enable TLS
        return properties;
    }

    /**
     * Identifierar vilken typ av innehåll e-postmeddelandet har och extraherar läsbar text från meddelandet.
     *
     * @param message Det e-postmeddelande som ska bearbetas.
     * @return Textinnehållet i e-postmeddelandet.
     * @throws MessagingException om ett fel uppstår under hantering av e-postmeddelandet.
     * @throws IOException        om det uppstår ett in- eller utdatafel.
     */
    public String extractMessageContent(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof Multipart) { //Om innehållet är ett multipart-message, iterera genom delarna i meddelandet och lägg till i StringBuilder - text
            Multipart multipart = (Multipart) content;
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.getContentType().toLowerCase().contains("text/plain")) {
                    text.append(bodyPart.getContent());
                }
            }
            return text.toString();
        } else { //Om innehållet inte är antingen en sträng eller Multipartmeddelande, returnera ett felmeddelande
            return "Unable to extract message content.";
        }
    }

    /**
     * Konverterar det senast mottagna mailet till enString, vilket gör den hanterbar för JtextArea att printa ut på ett snyggt sätt.
     *
     * @throws MessagingException om ett fel uppstår under hantering av e-postmeddelandet.
     * @throws IOException        om det uppstår ett in- eller utdatafel.
     */
    private void convertRecentMessageToString() throws MessagingException, IOException {
        String messageContent = extractMessageContent(mostRecentMessage);
        mostRecentMessageString = "Sent :" + mostRecentMessage.getSentDate() + "\n" + "From: " +
                mostRecentMessage.getFrom()[0] +
                "\n" + "Subject: " +
                mostRecentMessage.getSubject() +
                "\n" + "Message: " + messageContent +
                "\n" + "----------------------------------------" + "\n";
    }

    /**
     * Returnerar textrepresentationen av det senast mottagna e-postmeddelandet.
     *
     * @return Textrepresentationen av det senast mottagna e-postmeddelandet.
     */
    public String getMostRecentMessageString() {
        return mostRecentMessageString;
    }

}
