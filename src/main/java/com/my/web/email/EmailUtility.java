package com.my.web.email;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmailUtility {

    private static final Logger logger = Logger.getLogger(EmailUtility.class);
    private static final String recipientEmail = "cash.machine.epam2021@gmail.com";
    private static final String host = "smtp.gmail.com";

    private EmailUtility() {
    }

    public static void sendMail(String senderEmail, String filePath, ResourceBundle rb) throws MessagingException {
        logger.debug("sendMail method is started");
        System.out.println(filePath);

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(recipientEmail, "customPASS22");
            }
        });
        logger.debug("Received email session => " + session);
        session.setDebug(false);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(recipientEmail));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(senderEmail));

        message.setSubject(rb.getString("weekly.report.name"), "UTF-8");

        Multipart multipart = new MimeMultipart();
        MimeBodyPart attachment = new MimeBodyPart();
        MimeBodyPart text = new MimeBodyPart();

        try {
            File f = new File(filePath);
            attachment.attachFile(f);
            text.setText(rb.getString("send.report.email.text"), "UTF-8");
            multipart.addBodyPart(text);
            multipart.addBodyPart(attachment);
        } catch (IOException exception) {
            logger.error("An error has occurred while getting the report file : " + exception.getMessage());
        }

        message.setContent(multipart, "text/html; charset=UTF-8");

        logger.debug("Sending email ... ");
        Transport.send(message);
        logger.debug("Email sent successfully, sendMail method finished his work");
    }

}
