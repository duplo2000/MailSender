package Mailsender;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * MailClient-klassen representerar en e-postklient med användargränssnitt.
 * Användaren kan skriva och skicka e-postmeddelanden, med eller utan bifogad bilaga, samt hämta sina inkorgsmeddelanden.
 */
public class MailClient extends JFrame {
    private JTextField toField;
    private JTextField subjectField;
    private JTextField msgField;
    private JButton sendButton;
    private JLabel toLabel;
    private JLabel subjectLabel;
    private JLabel msgLabel;
    private JPanel emailAppPanel;
    private JTextField serverField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton inboxButton;
    private JButton chooseFileButton;
    private JLabel attachmentLabel;
    private JLabel fileNameLabel;
    private JPanel senderPanel;
    private JLabel emailIcon;
    private JLabel logInLabel;
    private String filename = "";
    private File file;

    /**
     * Huvudmetod för att starta e-postklienten
     */
    public static void main(String[] args) {
        MailClient email = new MailClient();
    }

    /**
     * Konstruktor för klienten som bygger användargränssnittet.
     * Skapar och initialiserar fönstret, knappar och textfält.
     */
    public MailClient() {
        setTitle("Eriks mail");
        setContentPane(emailAppPanel);
        setSize(666, 666);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        sendButton = createSendButton();
        inboxButton = createInboxButton();
        chooseFileButton = createChooseFileButton();
    }

    /**
     * Skapar knappen för att öppna inkorgen och kopplar den till öppningslogik.
     *
     * @return Inbox-knappen.
     */
    private JButton createInboxButton() {
        inboxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInboxWindow();
            }
        });
        return inboxButton;
    }

    /**
     * Öppnar fönstret som visar användarens inkorg med e-postmeddelanden.
     * Antingen med standard inbox eller användarens angivna gmail-konto.
     */
    private void openInboxWindow() {
        MailReceiver mailReceiver;
        try {
            mailReceiver = new MailReceiver(usernameField.getText(), passwordField.getText());
            // Hämta mail med genom mailReceivers receiveMail metod
            mailReceiver.receiveMail();
        } catch (MessagingException | IOException e) {
            JOptionPane.showMessageDialog(this, """
                    1. Check correct account credentials
                    2. Enable ~Less secure apps~
                    3. Use generated App Password
                    4. Check for Two Factor Authentication enabled""", "Error, log in failed.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //Konvertera inboxens mail till DisplayMessages
        List<DisplayMessage> displayMessages = mailReceiver.getMessagesToDisplay();
        new InboxWindow(displayMessages);
    }


    /**
     * Skapar Send-knappen för att skicka mail.
     *
     * @return Send-knappen.
     */
    private JButton createSendButton() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSendButtonClicked();
            }
        });
        return sendButton;
    }

    /**
     * Hanterar logiken när användaren klickar på "Send"-knappen.
     * Skapar en instans av MailSender, kopplar eventuell bilaga och skickar meddelandet.
     */
    private void handleSendButtonClicked() {
        try {
            MailSender mailSender = initializeMailSender();
            if (!filename.equals("")) {
                mailSender.setAttachment(file);
            }
            mailSender.sendMail();
            JOptionPane.showMessageDialog(this, "Email sent successfully!");
            toField.setText("");
            subjectField.setText("");
            msgField.setText("");
            fileNameLabel.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, """
                    1. Check correct account credentials
                    2. Enable ~Less secure apps~
                    3. Use generated App Password
                    4. Check for Two Factor Authentication enabled""", "Error, log in failed.", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Skapar knappen för att välja och bifoga en fil till e-postmeddelandet.
     *
     * @return Välj fil-knappen.
     */
    private JButton createChooseFileButton() {
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChooseFileButtonClicked();
            }
        });
        return chooseFileButton;
    }

    /**
     * Hanterar logiken när användaren väljer att bifoga en fil till meddelandet.
     */
    private void handleChooseFileButtonClicked() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        file = chooser.getSelectedFile();
        fileNameLabel.setText(file.getName());
    }

    /**
     * Förbereder och initialiserar MailSender-instansen med mottagare, ämne och meddelandeinnehåll.
     */
    private MailSender initializeMailSender() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String to = toField.getText();
        String subject = subjectField.getText();
        String msg = msgField.getText();

        MailSender mailSender = new MailSender(to, subject, msg, username, password);
        return mailSender;
    }
}
