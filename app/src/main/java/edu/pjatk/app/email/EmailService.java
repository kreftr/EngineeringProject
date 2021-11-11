package edu.pjatk.app.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
public class EmailService implements EmailSender {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    @Async
    public void send(String recipient, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(content, true);
            helper.setTo(recipient);
            helper.setSubject("Account activation");
            helper.setFrom("noreply@engineeringproject.com");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email");
        }
    }

    public String emailBuilder(String username, String activationLink, String htmlName){
        try{
            URL res = getClass().getClassLoader().getResource("templates/emails/"+htmlName);
            File htmlFile = Paths.get(res.toURI()).toFile();
            String htmlToString = Files.readString(Paths.get(htmlFile.getAbsolutePath()), StandardCharsets.UTF_8);
            htmlToString = htmlToString.replace("%USERNAME%", username);
            htmlToString = htmlToString.replace("%LINK%", activationLink);
            return htmlToString;
        } catch (Exception e){
            return activationLink + "\n" + "Error: " + e.getMessage();
        }
    }
}
