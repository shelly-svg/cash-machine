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

/**
 * Utility class for work with smtp
 *
 * @author Denys Tsebro
 */
public class EmailUtility {

    private static final Logger logger = Logger.getLogger(EmailUtility.class);
    private static final String SENDER_MAIL = "cash.machine.epam2021@gmail.com";
    private static final String SENDER_PASS = "customPASS22";
    private static final String HOST = "smtp.gmail.com";

    private EmailUtility() {
    }

    /**
     * Sends confirmation message with link to change user`s password
     *
     * @param recipientMail email address to which message will be sent
     * @param code          encrypted code to the page
     * @param rb            current users resource bundle to make localization
     * @throws MessagingException if message could not be sent
     */
    public static void sendPasswordLink(String recipientMail, String code, ResourceBundle rb) throws MessagingException {
        logger.debug("sendMail method is started");

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_MAIL, SENDER_PASS);
            }
        });

        logger.debug("Received email session => " + session);
        session.setDebug(false);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_MAIL));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientMail));

        message.setSubject(rb.getString("verify.password.change.email.title"), "UTF-8");

        String form = "<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" style=\"border-collapse:collapse;width:621px;color:#ffffff;background:#fdfdfd\">\n" +
                "\t<tbody>\n" +
                "\t<tr>\n" +
                "\t\t<td style=\"padding:0 40px 80px;font-size:10pt;color:#262222;font-family:tahoma,geneva,sans-serif\">\n" +
                "\t\t\t<p style=\"color:#262222\">" + rb.getString("verify.password.change.email.body.title")
                + ":</p>\n" +
                "\t\t\t<p style=\"color:#262222\">\n" +
                "\t\t\t<form action=\"http:/localhost:8080/cash_machine/controller\" method=\"get\">\n" +
                "\t\t\t<input type=\"hidden\" name=\"command\" value=\"changePassword\"/>\n" +
                "\t\t\t<input type=\"hidden\" name=\"code\" value=\"" + code
                + "\"/>\n" +
                "\t\t\t<input type=submit style=\"background-color: #45c0d5;color: white;padding: 16px 20px;margin: 8px 0;border: none;cursor: pointer;width: 100%;opacity: 0.9;\" value=\""
                + rb.getString("settings_jsp.change_pass_btn") + "\"/>\n" +
                "\t\t\t</form>\n" +
                "\t\t\t</p>\n" +
                "\t\t\t<p style=\"color:#262222\">" + rb.getString("verify.password.change.email.notify") + "</p>\n" +
                "\t\t</td>\n" +
                "\t</tr>\n" +
                "\t</tbody>\n" +
                "</table>";
        message.setContent(form, "text/html; charset=UTF-8");

        logger.debug("Sending email ... ");
        Transport.send(message);
        logger.debug("Email sent successfully, sendMail method finished his work");
    }

    /**
     * Send`s receipt for user
     *
     * @param recipientMail email address to which message will be sent
     * @param filePath      path to the report, to be sent
     * @param rb            current users resource bundle to make localization
     * @throws MessagingException if message could not be sent
     */
    public static void sendMail(String recipientMail, String filePath, ResourceBundle rb) throws MessagingException {
        logger.debug("sendMail method is started");

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_MAIL, SENDER_PASS);
            }
        });
        logger.debug("Received email session => " + session);
        session.setDebug(false);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_MAIL));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientMail));

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
