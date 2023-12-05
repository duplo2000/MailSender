package Mailsender;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

/**
 * Klassen InboxWindow representerar ett GUI-fönster som visar en inkorg med e-postmeddelanden.
 * Den visar en lista över e-postmeddelanden som användaren kan klicka på för att öppna och visa detaljer om meddelandet.
 */
public class InboxWindow extends JFrame {
    private JPanel inboxPanel;
    private List<DisplayMessage> displayMessages;

    /**
     * Skapar ett nytt Inkorgsfönster med en given lista av DisplayMessage-objekt.
     *
     * @param displayMessages En lista av DisplayMessage-objekt som ska visas i inkorgen.
     */
    public InboxWindow(List<DisplayMessage> displayMessages) {
        this.displayMessages = displayMessages;
        setTitle("Inbox");
        setSize(800, 400);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("INBOX");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        inboxPanel = new JPanel();
        inboxPanel.setLayout(new BoxLayout(inboxPanel, BoxLayout.Y_AXIS));
        JScrollPane jScrollPane = new JScrollPane(inboxPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        add(title);
        add(jScrollPane);
        populateInbox();

        setVisible(true);
    }

    /**
     * Fyller inkorgen med de 15 senast mottagna e-postmeddelanden från DisplayMessage-objekten.
     */
    public void populateInbox() {
        int count = 0;
        Collections.reverse(displayMessages);

        for (DisplayMessage d : displayMessages) {
            if (count >= 15) {
                break;
            }
            addEmailRow(inboxPanel, d);
            count++;
        }
    }

    /**
     * Lägger till en rad i inkorgen för ett DisplayMessage-objekt.
     *
     * @param inboxPanel     Panelen som representerar inkorgen.
     * @param displayMessage DisplayMessage-objektet som ska läggas till i inkorgen.
     */
    private void addEmailRow(JPanel inboxPanel, DisplayMessage displayMessage) {
        JPanel emailRow = new JPanel();
        emailRow.setLayout(new BoxLayout(emailRow, BoxLayout.X_AXIS));
        emailRow.setBorder(new EmptyBorder(10, 0, 10, 0));

        emailRow.addMouseListener(createEmailRowMouseListener(displayMessage));

        JLabel senderLabel = new JLabel("Sent: " + displayMessage.getSent() + " From: " + displayMessage.getFrom() + " Subject: " + displayMessage.getSubject());

        emailRow.add(senderLabel);

        addAttachmentIcon(emailRow, displayMessage);

        inboxPanel.add(emailRow);
        validate();
    }

    /**
     * Skapar en mouselistener för en e-postrad som öppnar ett nytt meddelandefönster när raden klickas på.
     *
     * @param displayMessage DisplayMessage-objektet som representerar e-postmeddelandet som ska öppnas.
     * @return En mouselistener för att hantera klickhändelser på e-postraden.
     */
    private MouseListener createEmailRowMouseListener(DisplayMessage displayMessage) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MessageWindow(displayMessage);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
    }

    /**
     * Lägger till en ikon för att indikera bilagor om e-postmeddelandet har det.
     *
     * @param emailRow       Panelen som representerar e-postraden.
     * @param displayMessage DisplayMessage-objektet som representerar e-postmeddelandet.
     */
    private void addAttachmentIcon(JPanel emailRow, DisplayMessage displayMessage) {
        if (displayMessage.hasAttachments()) {
            ImageIcon attachmentIcon = new ImageIcon("src/icons8-attachment-14.png");
            JLabel attachmentLabel = new JLabel(attachmentIcon);
            emailRow.add(attachmentLabel);
        }
    }
}