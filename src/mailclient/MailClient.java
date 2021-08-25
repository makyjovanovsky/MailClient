/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.mail.*;
import java.util.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 *
 * @author User
 */
public class MailClient extends JFrame implements ActionListener {

    /**
     * @param args the command line arguments
     */
    final private JPanel firstPanel;
    final private JPanel secondPanel;
    final private JPanel thirdPanel;
    final private JPanel fourthPanel;
    final private JPanel fifthPanel;
    final private JButton firstButton;
    final private JButton sendButton;
    final private JTextField firstTextField;
    final private JTextField secondTextField;
    final private JTextField thirdTextField;
    final private JTextArea area;
    final private JFrame frame;
    private String fileName;

    MailClient() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 300));
        frame.setTitle("Send message");
        frame.setLayout(new BorderLayout());

        area = new JTextArea();

        firstButton = new JButton("Browse...");
        firstButton.addActionListener(this);
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        firstTextField = new JTextField();
        secondTextField = new JTextField();
        thirdTextField = new JTextField();

        firstPanel = new JPanel(new GridLayout(3, 1));
        firstPanel.add(new JLabel("From: "));
        firstPanel.add(new JLabel("To: "));
        firstPanel.add(new JLabel("Subject: "));

        secondPanel = new JPanel(new GridLayout(3, 1));
        secondPanel.add(firstTextField);
        secondPanel.add(secondTextField);
        secondPanel.add(thirdTextField);

        thirdPanel = new JPanel(new GridLayout(3, 1));
        thirdPanel.add(firstButton);
 

        fourthPanel = new JPanel(new BorderLayout());
        fourthPanel.add(firstPanel, BorderLayout.WEST);
        fourthPanel.add(secondPanel, BorderLayout.CENTER);
        fourthPanel.add(thirdPanel, BorderLayout.EAST);

        fifthPanel = new JPanel(new FlowLayout());
        fifthPanel.add(sendButton);

        frame.add(fourthPanel, BorderLayout.NORTH);
        frame.add(area, BorderLayout.CENTER);
        frame.add(fifthPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        
        fileName = "";
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        String proba = "";
        if (event.getSource() == firstButton) {
            JFileChooser temp = new JFileChooser();
            temp.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = temp.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = temp.getSelectedFile();
                fileName = selectedFile.getAbsolutePath();
            }
        }
        if (event.getSource() == sendButton) {
            String from = firstTextField.getText();
            String to = secondTextField.getText();
            String subject = thirdTextField.getText();
            String message = area.getText();
            String host = "localhost";

            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);

            Session session = Session.getDefaultInstance(properties);

            try {
                MimeMessage mime = new MimeMessage(session);
                mime.setFrom(new InternetAddress(from));
                mime.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mime.setSubject(subject);

                if (!fileName.equals(proba)) {

                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText(message);

                    MimeBodyPart attachment = new MimeBodyPart();
                    attachment.attachFile(new File(fileName));

                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);
                    multipart.addBodyPart(attachment);

                    mime.setContent(multipart);
                } else {
                    mime.setText(message);
                }
                if (from.contains("@") && from.contains(".")) {
                    if (to.contains("@") && to.contains(".")) {
                        Transport.send(mime);
                        System.exit(0);
                     } else {
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                
            } catch (Exception mex) {
                mex.printStackTrace();
            }


        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        MailClient client = new MailClient();
    }

}
