package Mailsender;

import java.util.ArrayList;
import java.util.List;

/**
 * Klassen DisplayMessage används för att representera ett e-postmeddelande som ska visas i användargränssnittet.
 * Den innehåller information om meddelandets avsändare, ämne, innehåll och eventuella bilagor.
 */
public class DisplayMessage {
    public final String sent;
    private final String from;
    private final String subject;
    private String message;
    private List<Attachment> attachments;

    /**
     * Konstruerar ett DisplayMessage med angiven avsändare, ämne, meddelande och eventuella bilagor.
     *
     * @param sent    Datum och tid då meddelandet skickades.
     * @param from    Avsändarens e-postadress.
     * @param subject Ämnet för meddelandet.
     * @param message Innehållet i meddelandet.
     */
    public DisplayMessage(String sent, String from, String subject, String message) {
        this.sent = sent;
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.attachments = new ArrayList<>();
    }

    /**
     * Hämtar avsändarens e-postadress.
     *
     * @return E-postadressen för avsändaren.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Hämtar datum och tid då meddelandet skickades.
     *
     * @return Datum och tid då meddelandet skickades.
     */
    public String getSent() {
        return sent;
    }

    /**
     * Hämtar ämnet för meddelandet.
     *
     * @return Ämnet för meddelandet.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Hämtar innehållet i meddelandet.
     *
     * @return Innehållet i meddelandet.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sätter bilagor för meddelandet.
     *
     * @param attachments En lista av Attachment-objekt som representerar bilagor.
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * Hämtar en lista av bilagor som är bifogade till meddelandet.
     *
     * @return En lista av Attachment-objekt som representerar bilagor.
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * Kontrollerar om meddelandet har några bifogade bilagor.
     *
     * @return true om meddelandet har bilagor, annars false.
     */
    public boolean hasAttachments() {
        return !attachments.isEmpty();
    }
}
