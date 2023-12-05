package Mailsender;


import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Klassen MessageWindow representerar ett fönster som visar ett epostmeddelande, och möjlighet för användaren att ladda ned eventuella bilagor.
 */
public class MessageWindow extends JFrame {

    private final DisplayMessage displayMessage;

    /**
     * Skapar en ny instans av MessageWindow med det aktuella DisplayMessage:t.
     *
     * @param displayMessage Meddelandet att visa i fönstret.
     */
    public MessageWindow(DisplayMessage displayMessage) {
        this.displayMessage = displayMessage;

        setTitle("Message from " + displayMessage.getFrom());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = createMainPanel();
        add(mainPanel);

        setVisible(true);
    }

    /**
     * Skapar huvudpanelen för meddelandefönstret.
     *
     * @return En JPanel som innehåller meddelandeinnehållet och eventuella nedladdningsknappar.
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel messagePanel = createMessagePanel();
        mainPanel.add(messagePanel);

        if (displayMessage.hasAttachments()) {
            JPanel downloadPanel = createDownloadPanel();
            mainPanel.add(downloadPanel);
        }

        return mainPanel;
    }

    /**
     * Skapar en panel för textinnehållet.
     *
     * @return En JPanel som innehåller meddelandeinnehållet.
     */
    private JPanel createMessagePanel() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        JTextArea messageContent = createMessageContent();
        JScrollPane messageScrollPane = new JScrollPane(messageContent);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messagePanel.add(messageScrollPane);

        return messagePanel;
    }

    /**
     * Skapar en JTextArea för meddelandeinnehållet.
     *
     * @return En JTextArea som visar meddelandeinnehållet och möjliggör scrollning vid behov.
     */
    private JTextArea createMessageContent() {
        JTextArea messageContent = new JTextArea(displayMessage.getMessage());
        messageContent.setWrapStyleWord(true);
        messageContent.setLineWrap(true);
        messageContent.setEditable(false);
        return messageContent;
    }

    /**
     * Skapar en panel för att visa bilagor.
     *
     * @return En JPanel som kan innehålla knappar för att ladda ner bilagor.
     */
    private JPanel createDownloadPanel() {
        JPanel downloadPanel = new JPanel();

        for (Attachment attachment : displayMessage.getAttachments()) {
            JButton downloadButton = createDownloadButton(attachment);
            downloadPanel.add(downloadButton);
        }

        return downloadPanel;
    }

    /**
     * Skapar en knapp för att ladda ner en bilaga.
     *
     * @param attachment Bilagan som ska laddas ner.
     * @return En JButton som användaren kan klicka på för att ladda ner bilagan.
     */
    private JButton createDownloadButton(Attachment attachment) {
        ImageIcon downloadIcon = new ImageIcon("src/icons8-download-20.png");
        JButton downloadButton = new JButton("Download " + attachment.getFileName());
        downloadButton.setIcon(downloadIcon);

        downloadButton.addActionListener(e -> downloadAttachment(attachment));

        return downloadButton;
    }

    /**
     * Laddar ner en bilaga och sparar den på användarens system med hjälp av JFileChooser.
     *
     * @param attachment Bilagan som ska laddas ner.
     */
    private void downloadAttachment(Attachment attachment) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileChooser.getSelectedFile())) {
                fileOutputStream.write(attachment.getData());
                fileOutputStream.flush();
                JOptionPane.showMessageDialog(this, "Attachment downloaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
