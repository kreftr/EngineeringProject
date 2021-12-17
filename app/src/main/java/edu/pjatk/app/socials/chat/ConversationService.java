package edu.pjatk.app.socials.chat;

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
    private final UserRepository userRepository;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, UserRepository userRepository){
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    public Optional<Conversation> findConversationById(Long id) {
        return conversationRepository.findConversationById(id);
    }

    public Optional <Conversation> findConversationByUserId(Long first_user_id, Long second_user_id) {
        return conversationRepository.findConversationByUserId(first_user_id, second_user_id);
    }

    public void createConversation(Long first_user_id, Long second_user_id) {
        Optional<User> first_user = userRepository.findById(first_user_id);
        Optional<User> second_user = userRepository.findById(second_user_id);
        Optional<Conversation> existing_conversation = conversationRepository.findConversationByUserId(first_user_id, second_user_id);
        if (first_user.isPresent() && second_user.isPresent() && existing_conversation.isEmpty()) {
            Conversation conversation = new Conversation(first_user.get(), second_user.get());
            conversationRepository.createConversation(conversation);
        }
    }

    public void addMessage(Long conversation_id, Long author_id, String text){
        Date date = java.util.Calendar.getInstance().getTime();
        Optional<Conversation> conversation = conversationRepository.findConversationById(conversation_id);
        Optional<User> author = userRepository.findById(author_id);
        if (conversation.isPresent() && author.isPresent())
        {
            Message message = new Message(text, date, conversation.get(), author.get());
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
