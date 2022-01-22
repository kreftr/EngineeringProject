package edu.pjatk.app.socials.chat;

import edu.pjatk.app.response.ConversationResponse;
import edu.pjatk.app.response.MessageResponse;
import edu.pjatk.app.response.RecentMessageResponse;
import edu.pjatk.app.response.ResponseMessage;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserService userService;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, UserService userService){
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }

    public Optional<Conversation> getConversationById(Long id) {
        return conversationRepository.getConversationById(id);
    }
    public Optional<Conversation> getConversationByUserId(Long user_id) {
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (loggedUser.isPresent()) {
            return conversationRepository.getConversationByUserId(user_id, loggedUser.get().getId());
        }
        else return Optional.empty();
    }

    public Optional<ConversationResponse> getConversationResponseByUserId(Long user_id) {
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        if (loggedUser.isEmpty()) { return Optional.empty(); }

        Optional<Conversation> conversationOptional = conversationRepository.getConversationByUserId(user_id, loggedUser.get().getId());
        if (conversationOptional.isEmpty()) { return Optional.empty(); }

        // user we are talking to
        Optional<User> optional_target_user = userService.findUserById(user_id);
        if (optional_target_user.isEmpty()) { return Optional.empty(); }

        return Optional.of(new ConversationResponse(conversationOptional.get().getId(), user_id,
                optional_target_user.get().getUsername(), optional_target_user.get().getProfile().getPhoto().getFileName()));
    }

    public List<Conversation> getAllUserConversations(){

        List<Conversation> conversations = new ArrayList<>();

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<List<Conversation>> optionalConversations = conversationRepository.getAllUserConversations(
                loggedUser.get().getId()
        );

        if (optionalConversations.isPresent() && !optionalConversations.get().isEmpty()){
            for (Conversation c : optionalConversations.get()){
                conversations.add(c);
            }
        }

        return conversations;
    }

    public Optional<List<Conversation>> getAllUserConversations(Long user_id) {
        Optional<List<Conversation>> conversations;
        conversations = conversationRepository.getAllUserConversations(user_id);
        return conversations;
    }

    public void createConversation(Long user_id) {
        Optional<User> first_user = userService.findUserById(user_id);
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Conversation> existing_conversation = conversationRepository.getConversationByUserId(
                user_id, loggedUser.get().getId()
        );
        if (first_user.isPresent() && loggedUser.isPresent() && existing_conversation.isEmpty()) {
            Conversation conversation = new Conversation(loggedUser.get(), first_user.get());
            conversationRepository.createConversation(conversation);
        }
    }

    public void addMessage(Long conversation_id, Long author_id, String text){
        Optional<Conversation> conversation = conversationRepository.getConversationById(conversation_id);
        Optional<User> author = userService.findUserById(author_id);
        if (conversation.isPresent() && author.isPresent())
        {
            Message message = new Message(text, LocalDateTime.now(), conversation.get(), author.get());
            conversationRepository.addMessage(message);
        }
    }

    public void deleteById(Long id){ conversationRepository.deleteById(id); }

    public void removeConversation(Conversation conversation){
        conversationRepository.remove(conversation);
    }

    public Optional<List<MessageResponse>> getAllMessages(Long id){
        Optional<List<Message>> optionalMessages = conversationRepository.getAllMessages(id);
        if (optionalMessages.isEmpty()) { return Optional.empty(); }

        List<MessageResponse> messageResponses = new ArrayList<>();
        for (Message message: optionalMessages.get()) {
            MessageResponse tempResponse = new MessageResponse(message.getContent(), message.getUser().getUsername(),
                    message.getMessage_date().toString(), message.getUser().getProfile().getPhoto().getFileName());
            messageResponses.add(tempResponse);
        }

        return Optional.of(messageResponses);
    }

    public Optional<RecentMessageResponse> getRecentMessage(Long id){

        Optional<Message> message = conversationRepository.getRecentMessage(id);

        if (message.isPresent()){

            String messageContent;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            if(message.get().getContent().length() <= 12) messageContent = message.get().getContent();
            else messageContent = message.get().getContent().substring(0, 12)+"...";

            return Optional.of(
              new RecentMessageResponse(
                      message.get().getUser().getUsername(),
                      message.get().getMessage_date().format(formatter),
                      messageContent
              )
            );
        }
        else return Optional.empty();
    }

    public List<ConversationResponse> getAllUserConversationResponse() {

        List<ConversationResponse> listToReturn = new ArrayList<>();

        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (loggedUser.isPresent()){
            Optional<List<Conversation>> conversations = conversationRepository.getAllUserConversations(
                    loggedUser.get().getId()
            );
            if (conversations.isEmpty()) return Collections.emptyList();
            else {
                String photoUrl;
                for (Conversation c : conversations.get()){
                    if (c.getFirst_user().equals(loggedUser.get())){
                        try {
                            photoUrl = c.getSecond_user().getProfile().getPhoto().getFileName();
                        } catch (NullPointerException e){
                            photoUrl = null;
                        }
                        listToReturn.add(
                                new ConversationResponse(
                                        c.getId(),
                                        c.getSecond_user().getId(),
                                        c.getSecond_user().getUsername(),
                                        photoUrl
                                )
                        );
                    }
                    else {
                        try {
                            photoUrl = c.getFirst_user().getProfile().getPhoto().getFileName();
                        } catch (NullPointerException e){
                            photoUrl = null;
                        }
                        listToReturn.add(
                                new ConversationResponse(
                                        c.getId(),
                                        c.getFirst_user().getId(),
                                        c.getFirst_user().getUsername(),
                                        photoUrl
                                )
                        );
                    }
                }
                return listToReturn;
            }
        }
        else return Collections.emptyList();
    }

}
