package edu.pjatk.app.socials;

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

    public Optional<Conversation> findConversationById(Long id) {
        return conversationRepository.findConversationById(id);
    }

    public void addMessage(Long conversation_id, String text){
        Date date = java.util.Calendar.getInstance().getTime();
        Optional<Conversation> conversation = conversationRepository.findConversationById(conversation_id);
        if (conversation.isPresent())
        {
            Message message = new Message(text, date, conversation.get());
            conversationRepository.addMessage(message);
        }
    }

    public void deleteById(Long id){ conversationRepository.deleteById(id); }

    public Optional<List<Message>> getAllMessages(Long id){
        return conversationRepository.getAllMessages(id);
    }

    public Optional<Message> getRecentMessage(Long id){
        return conversationRepository.getRecentMessage(id);
    }

    public Optional<List<Conversation>> getAllUserConversations(Long userId) {
        return conversationRepository.getAllUserConversations(userId);
    }
}
