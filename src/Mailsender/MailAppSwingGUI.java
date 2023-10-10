package Mailsender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MailAppSwingGUI extends JFrame {
    private JTextField toField;
    private JTextField subjectField;
    private JTextField msgField;
    private JButton sendButton;
    private JLabel toLabel;
    private JLabel subjectLabel;
    private JLabel msgLabel;
    private JLabel mailAppLabel;
    private JTextArea inboxDisplay;
    private JPanel emailAppPanel;


    public MailAppSwingGUI(){
        setTitle("Mail App");
        setContentPane(emailAppPanel);
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        sendButton = createSendButton();
    }
    private JButton createSendButton() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSendButtonClicked();
            }
        });
        return sendButton;
    }
    private void handleSendButtonClicked() {
        String emailRecipient = toField.getText();
        String emailSubject = subjectField.getText();
        String emailMsg = msgField.getText();
        MailSender mailSender = new MailSender(emailRecipient, emailSubject, emailMsg);
        mailSender.sendMail();
    }

    public static void main(String[] args) {
        MailAppSwingGUI email = new MailAppSwingGUI();
    }
}
