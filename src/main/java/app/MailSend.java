package app;

import java.util.Properties;
import java.util.Date;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Session;
import javax.mail.Transport;

public class MailSend {

    public static void main(String[] args) {

        try {

            String return_path = "bounces@mail.com";

            InternetAddress from = new InternetAddress("user@mail.com", "Eriton Almeida");

            String smtp_host = "smtp.sendgrid.net";
            String smtp_port = "587";

            String smtp_username = "apikey";
            String smtp_password = "api_secret";

            Properties config = new Properties();
            config.put("mail.smtp.auth", "true");
            config.put("mail.smtp.starttls.enable", "true");
            config.put("mail.smtp.host", smtp_host);
            config.put("mail.smtp.port", smtp_port);
            config.put("mail.smtp.from", return_path);

            Session session = Session.getDefaultInstance(config);

            Transport smtp = session.getTransport("smtp");

            smtp.connect(smtp_username, smtp_password);

            String[] recipients = {"erytonalmeida@gmail.com", "eritona@yahoo.com"};

            String template = "<p>Hi {email}, this is email test.</p>";

            for (String to : recipients) {

                Message message = new MimeMessage(session);

                message.setFrom(from);

                message.setSentDate(new Date());

                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

                message.addHeader("Track-Id", "123-45-6");

                message.setSubject("JavaMail Test");

                MimeMultipart content = new MimeMultipart("alternative");
                MimeBodyPart textPart = new MimeBodyPart();
                MimeBodyPart htmlPart = new MimeBodyPart();

                String html = template.replace("{email}", to);
                String text = html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "\r\n");

                textPart.setContent(text, "text/plain; charset=utf-8");
                htmlPart.setContent(html, "text/html; charset=utf-8");

                content.addBodyPart(textPart);
                content.addBodyPart(htmlPart);

                message.setContent(content);

                smtp.sendMessage(message, message.getAllRecipients());

                System.out.println(to + ": Ok");
            }

            smtp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
