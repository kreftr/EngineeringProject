package edu.pjatk.app.socials;


import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository){
        this.conversationRepository = conversationRepository;
    }

    public void addMessage(String text){
        Date date = java.util.Calendar.getInstance().getTime();
        Message message = new Message(text, date);
        conversationRepository.addMessage(message);

    }

    public void deleteById(Long id){ conversationRepository.deleteById(id); }

    public Optional<List<Message>> getAllMessages(){
        return conversationRepository.getAllMessages();
    }

    public Optional<Message> getRecentMessage(){
        return conversationRepository.getRecentMessage();
    }
}
