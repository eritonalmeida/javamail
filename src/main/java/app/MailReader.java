package app;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class MailReader {

    public static void main(String[] args) {

        try {
            String pop3_username = "pop@mail.com";
            String pop3_password = "password";

            String pop3_host = "pop3.mail.com";
            String pop3_port = "110";

            Properties config = new Properties();
            config.put("mail.pop3.host", pop3_host);
            config.put("mail.pop3.port", pop3_port);

            Session session = Session.getDefaultInstance(config);

            Store pop3 = session.getStore("pop3");

            pop3.connect(pop3_username, pop3_password);

            Folder folder = pop3.getFolder("INBOX");

            folder.open(Folder.READ_WRITE);

            int count = folder.getMessageCount();

            System.out.println("Messages: " + count);

            if (count > 10) {
                count = 10;
            }

            for (int id = 1; id < count; id++) {
                Message message = folder.getMessage(id);

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                message.writeTo(out);

                String body = new String(out.toByteArray(), "UTF-8");

                Pattern pattern = Pattern.compile("Track\\-Id:\\s(.*)");
                Matcher matcher = pattern.matcher(body);

                if (body.contains("my string") && matcher.find()) {
                    System.out.println(matcher.group(1));
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }

            //expunges messages deleted
            folder.close(true);
            pop3.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
