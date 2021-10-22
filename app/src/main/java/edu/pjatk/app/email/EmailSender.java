package edu.pjatk.app.email;

public interface EmailSender {
    void send(String recipient, String content);
}
