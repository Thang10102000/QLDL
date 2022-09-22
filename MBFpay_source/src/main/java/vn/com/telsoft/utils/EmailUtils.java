package vn.com.telsoft.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtils {
    public static void sendMail(List<String> toEmail, String fromEmail, String subject, String content, String userName,
                                String password, String host, String port, String exelPath, String pdfPath, String newFileName) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);

        // Get the Session object.
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromEmail,"Hệ thống đối soát MobiFone Money", "utf-8"));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", toEmail)));

            // Set Subject: header field
            message.setSubject(subject, "utf-8");

            // ========= MAIL CONTENT ================
            Multipart multipart = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(content, "text/html; charset=utf-8");
            multipart.addBodyPart(textBodyPart);

            // PDF attachment
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
//            DataSource source = new FileDataSource("C:\\Users\\thong.nguyenminh\\IdeaProjects\\mbfpay_ds\\source\\src\\main\\java\\vn" +
//                    "\\com\\telsoft\\utils\\BIDV_2022-05-01_2022-05-31.pdf"); // ex : "C:\\test.pdf"
            DataSource source = new FileDataSource(pdfPath); // ex : "C:\\test.pdf"
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(newFileName + ".pdf"); // ex : "test.pdf"

            multipart.addBodyPart(attachmentBodyPart);

            // EXEL attachment
            MimeBodyPart attachmentBodyPart2 = new MimeBodyPart();
//            DataSource source2 = new FileDataSource("C:\\Users\\thong.nguyenminh\\IdeaProjects\\mbfpay_ds\\source\\src\\main\\java\\vn" +
//                    "\\com\\telsoft\\utils\\BIDV_2022-05-01_2022-05-31.xlsx");
            DataSource source2 = new FileDataSource(exelPath);
            attachmentBodyPart2.setDataHandler(new DataHandler(source2));
            attachmentBodyPart2.setFileName(newFileName + ".xlsx"); // ex : "test.pdf"

            multipart.addBodyPart(attachmentBodyPart2);

            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}