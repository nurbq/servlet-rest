package lab.andersen.service;

import lombok.Getter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class EmailSenderService {

    @Getter
    private static final EmailSenderService INSTANCE = new EmailSenderService();
    private static final Logger logger = Logger.getLogger(EmailSenderService.class.getName());

    private EmailSenderService() {
    }

    public void sendEmail(String emailTo, String emailPassword, String emailFrom,
                          String bodyMessage,
                          String subjectMessage,
                          String fullPathPdf) {


        Properties props = getProperties();

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        emailFrom,
                        emailPassword);
            }
        });

        try {
            Transport transport = session.getTransport();
            InternetAddress addressFrom = new InternetAddress(emailFrom);

            Message message = new MimeMessage(session);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(bodyMessage, "utf-8", "html");
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            attachmentBodyPart.attachFile(
                    new File(fullPathPdf));
            multipart.addBodyPart(attachmentBodyPart);

            message.addHeader("Content-Disposition", "attachment;");
            message.setFrom(addressFrom);
            message.setSubject(subjectMessage);
            message.setContent(multipart);

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailTo));

            transport.connect();
            Transport.send(message);
            transport.close();

            logger.info("Email has sent successfully");
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.net", "javax.net");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }
}
