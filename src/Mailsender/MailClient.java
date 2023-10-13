package Mailsender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klassen MailClient representerar ett enkelt e-postklientgränssnitt. Det möjliggör för användaren att
 * skicka och ta emot e-postmeddelanden, samt visa mottagna meddelanden i ett JTextArea-fönster.
 */
public class MailClient extends JFrame {
    private JTextField toField;
    private JTextField subjectField;
    private JTextField msgField;
    private JButton sendButton;
    private JLabel toLabel;
    private JLabel subjectLabel;
    private JLabel msgLabel;
    private JLabel mailAppLabel;
    private JPanel emailAppPanel;
    private JButton refreshButton;
    private JTextArea receivedMails;
    private JTextField serverField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JLabel mailServerLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JScrollPane scrollpane;

    /**
     * Huvudmetod för att starta e-postklienten
     */
    public static void main(String[] args) {
        MailClient email = new MailClient();
    }

    /**
     * Konstruktor för klienten som bygger GUI:n
     */
    public MailClient() {
        setTitle("Mail App");
        setContentPane(emailAppPanel);
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        receivedMails.setEditable(false);
        sendButton = createSendButton();
        refreshButton = createRefreshButton();
    }

    /**
     * Skapar "Refresh" knappen för att ta emot nya mail.
     *
     * @return Returnerar refresh-knappen.
     */
    private JButton createRefreshButton() {
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRefreshButtonClicked();
            }
        });
        return refreshButton;
    }

    /**
     * Hanterar logiken när användaren trycker på refreshknappen genom att skapa en instans av MailSender och anropa metoderna receiveMail & getMostRecentMessageString
     */
    private void handleRefreshButtonClicked() {

        try {
            if (serverField != null || usernameField != null || passwordField != null) {
                MailReceiver mailReceiver = new MailReceiver(serverField.toString(), usernameField.toString(), passwordField.toString());
            }
            // Hämta mail med genom mailReceivers receiveMail metod
            MailReceiver mailReceiver = new MailReceiver();
            mailReceiver.receiveMail();

            String mostRecentMail = mailReceiver.getMostRecentMessageString();

            //Rensa textArean receivedMails
            receivedMails.setText("");
            //Visa senaste mailet på receivedMails
            receivedMails.append(mostRecentMail);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Hanterar logiken när användaren klickar på knappen genom att skapa en instans av MailSender och anropa dess metod sendMail, och bekräfta att meddelandet har skickats.
     */
    private void handleSendButtonClicked() {
        String emailRecipient = toField.getText();
        String emailSubject = subjectField.getText();
        String emailMsg = msgField.getText();
        MailSender mailSender = new MailSender(emailRecipient, emailSubject, emailMsg);
        mailSender.sendMail();
        receivedMails.append("Client: Email sent succesfully!");
    }


}
