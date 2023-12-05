package Mailsender;

import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private Message[] messages;
    private Folder inbox;
    private Store store;

    private List<DisplayMessage> displayMessages = new ArrayList<>();
    /**
     * Konstruktor med användarens inställningar.
     *
     * @param receiveUsername Användarnamn för e-postkontot.
     * @param receivePassword Lösenord för e-postkontot.
     */

    public MailReceiver(String receiveUsername, String receivePassword) {
        this.receiveUsername = receiveUsername;
        this.receivePassword = receivePassword;
        receiveHost = "imap.gmail.com";
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
        store = session.getStore("imaps");

        store.connect(receiveHost, receiveUsername, receivePassword);

        inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        messages = inbox.getMessages();

        displayMessages = extractDisplayMessages(messages);

        closeSession();
    }


    /**
     * Konfigurerar e-postinställningarna, inklusive protokoll och serverinformation för IMAP.
     *
     * @return Properties-objekt som innehåller inställningarna.
     */
    private Properties configureProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        properties.setProperty("mail.imaps.host", receiveHost);
        properties.setProperty("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.starttls.enable", "true");
        return properties;
    }
    /**
     * Stänger sessionen och frigör resurser efter att e-postmeddelanden har hämtats.
     *
     * @throws MessagingException om det uppstår problem vid stängning av sessionen.
     */
    public void closeSession() throws MessagingException {
        inbox.close(false);
        store.close();
    }
    /**
     * Extraherar textinnehållet från ett e-postmeddelande.
     *
     * @param message E-postmeddelandet att extrahera innehållet från.
     * @return Textinnehållet av e-postmeddelandet.
     * @throws MessagingException om det uppstår problem vid hantering av meddelandet.
     * @throws IOException om det uppstår ett in- eller utdatafel.
     */
    public String extractTextualContent(Message message) throws MessagingException, IOException {
        Object content = message.getContent();

        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof Multipart) {
            return extractTextFromMultipart((Multipart) content);
        } else {
            return content.toString();
        }
    }
    /**
     * Extraherar textinnehållet från ett multipart-meddelande.
     *
     * @param multipart Multipart-meddelandet att extrahera innehållet från.
     * @return Textinnehållet av multipart-meddelandet.
     * @throws MessagingException om det uppstår problem vid hantering av meddelandet.
     * @throws IOException om det uppstår ett in- eller utdatafel.
     */
    private String extractTextFromMultipart(Multipart multipart) throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);

            if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
                // Om det är plain text eller html-text, returnera strängen som den är
                return bodyPart.getContent().toString();
            } else if (bodyPart.getContent() instanceof Multipart) {
                String textContent = extractTextFromMultipart((Multipart) bodyPart.getContent());
                if (textContent != null) {
                    return textContent;
                }
            }
        }
        return "";
    }


    /**
     * @return Array av alla mottagna meddelanden.
     */
    public List<DisplayMessage> getMessagesToDisplay() {
        return displayMessages;
    }
    /**
     * Returnerar en lista av alla mottagna meddelanden i form av DisplayMessage-objekt.
     *
     * @return En lista av DisplayMessage-objekt som representerar de mottagna meddelandena.
     * @throws MessagingException om det uppstår problem vid hantering av meddelandena.
     * @throws IOException om det uppstår ett in- eller utdatafel.
     */
    private List<DisplayMessage> extractDisplayMessages(Message[] messages) throws MessagingException, IOException {
        List<DisplayMessage> displayMessages = new ArrayList<>();
        for (Message message : messages) {
            String sent = message.getSentDate().toString();
            String from = message.getFrom()[0].toString();
            String subject = message.getSubject();
            String messageContent = extractTextualContent(message);

            AttachmentReceiver attachmentReceiver = new AttachmentReceiver(message);
            List<Attachment> attachments = attachmentReceiver.extractAttachments();

            DisplayMessage displayMessage = new DisplayMessage(sent, from, subject, messageContent);
            displayMessage.setAttachments(attachments);

            displayMessages.add(displayMessage);
        }
        return displayMessages;
    }

}
