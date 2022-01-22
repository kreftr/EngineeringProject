package edu.pjatk.app.socials.chat;

import edu.pjatk.app.response.*;
import edu.pjatk.app.timestamp.Timestamp;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping(value = "/createConversation/{user_id}")
    public ResponseEntity<?> createConversation(@PathVariable Long user_id) {
        conversationService.createConversation(user_id);
        return new ResponseEntity<>(
                new ResponseMessage("Conversation created!"), HttpStatus.OK
        );
    }

    @GetMapping(value = "/getConversationById/{id}")
    public ResponseEntity<?> getConversationById(@PathVariable Long id) {
        Optional<Conversation> conversation = conversationService.getConversationById(id);
        if (conversation.isPresent())
        {
            return new ResponseEntity<>(
                    conversation, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no conversations!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getConversationByUserId/{user_id}")
    public ResponseEntity<?> getConversationByUserId(@PathVariable Long user_id) {
        Optional<ConversationResponse> conversation = conversationService.getConversationResponseByUserId(user_id);
        if (conversation.isPresent())
        {
            return new ResponseEntity<>(
                    conversation, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There is no conversation between these people!"), HttpStatus.NOT_FOUND
            );
        }
    }


    @PostMapping(value = "/addMessage/{conversation_id}/{author_id}/{text}")
    public ResponseEntity<?> addMessage(@PathVariable Long conversation_id, @PathVariable Long author_id, @PathVariable String text) {
        String trimmedText = text.trim();
        if (trimmedText.length() > 0)  // disables sending empty messages
        {
            conversationService.addMessage(conversation_id, author_id, text);
            return new ResponseEntity<>(
                    new ResponseMessage("Message uploaded!"), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("Message is empty!"), HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        conversationService.deleteById(id);
        return new ResponseEntity<>(
                new ResponseMessage("Message deleted!"), HttpStatus.OK
        );
    }

    @GetMapping(value = "/getAllMessages/{id}")
    public ResponseEntity<?> getAllMessages(@PathVariable long id) {
        Optional<List<MessageResponse>> messages;
        messages = conversationService.getAllMessages(id);
        if (messages.isPresent())
        {
            return new ResponseEntity<>(
                    messages.get(), HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no messages!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getRecentMessage/{id}")
    public ResponseEntity<?> getRecentMessage(@PathVariable Long id) {
        Optional<RecentMessageResponse> message = conversationService.getRecentMessage(id);
        if (message.isPresent())
        {
            return new ResponseEntity<>(
                    message, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no messages!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getAllUserConversations")
    public ResponseEntity<?> getAllUserConversations() {
        List<ConversationResponse> conversations = conversationService.getAllUserConversationResponse();

        if (!conversations.isEmpty())
        {
            return new ResponseEntity<>(
                    conversations, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no conversations!"), HttpStatus.NOT_FOUND
            );
        }
    }

}
