package Mailsender;

import javax.mail.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klassen AttachmentReceiver används för att extrahera bilagor från ett e-postmeddelande.
 */
public class AttachmentReceiver {
    private Message message;

    /**
     * Konstruerar en AttachmentReceiver med angivet e-postmeddelande.
     *
     * @param message E-postmeddelandet från vilket bilagor ska extraheras.
     */
    public AttachmentReceiver(Message message) {
        this.message = message;
    }

    /**
     * Extraherar bilagor från e-postmeddelandet och returnerar dem som en lista av Attachment-objekt.
     *
     * @return En lista av Attachment-objekt som representerar bilagorna i e-postmeddelandet.
     * @throws MessagingException om det uppstår problem med hanteringen av e-postmeddelandet.
     * @throws IOException        om det uppstår problem med in- eller utdata vid extrahering av bilagor.
     */
    public List<Attachment> extractAttachments() throws MessagingException, IOException {
        List<Attachment> attachments = new ArrayList<>();
        Object content = message.getContent();

        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    String fileName = bodyPart.getFileName();
                    if (fileName != null && !fileName.isEmpty()) {
                        // Read the attachment data into a byte array
                        byte[] data = new byte[bodyPart.getSize()];
                        bodyPart.getInputStream().read(data);

                        attachments.add(new Attachment(fileName, data));
                    }
                }
            }
        }
        return attachments;
    }
}

