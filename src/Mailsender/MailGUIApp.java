//package Mailsender;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class MailGUIApp extends JFrame {
//    private JTextField toField;
//    private JTextField subjectField;
//    private JTextField msgArea;
//
//    public static void main(String[] args) {
//        MailGUIApp mailGUI = new MailGUIApp();
//        mailGUI.setVisible(true);
//    }
//
//    //Konstruktorn skapar hela gränssnittet
//    public MailGUIApp() {
//        setTitle("MailSender");
//        setSize(666, 300);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        JPanel mainPanel = new JPanel(new BorderLayout());
//
//        JPanel formPanel = new JPanel(new GridLayout(1, 2));
//        JLabel toLabel = new JLabel("To:");
//        JLabel subjectLabel = new JLabel("Subject:");
//        JLabel messageLabel = new JLabel("Message:");
//
//        toField = new JTextField();
//        subjectField = new JTextField();
//        msgArea = new JTextField();
//
//        JButton sendButton = createSendButton();
//
//        formPanel.add(toLabel);
//        formPanel.add(toField);
//        formPanel.add(subjectLabel);
//        formPanel.add(subjectField);
//
//        mainPanel.add(formPanel, BorderLayout.NORTH);
//        mainPanel.add(messageLabel, BorderLayout.WEST);
//        mainPanel.add(msgArea, BorderLayout.EAST);
//        mainPanel.add(new JScrollPane(msgArea));
//
//        mainPanel.add(sendButton, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//
//    //Skapa knappen för att skicka iväg mailet
//    private JButton createSendButton() {
//        JButton sendButton = new JButton("Send");
//        sendButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                handleSendButtonClicked();
//            }
//        });
//        return sendButton;
//    }
//
//    //Använd argumenten från fälten, skicka till instansen av mailsender för att skicka mailet när send-knappen trycks på
//    private void handleSendButtonClicked() {
//        String emailRecipient = toField.getText();
//        String emailSubject = subjectField.getText();
//        String emailMsg = msgArea.getText();
//        MailSender mailSender = new MailSender(emailRecipient, emailSubject, emailMsg);
//        mailSender.sendMail();
//    }
//}