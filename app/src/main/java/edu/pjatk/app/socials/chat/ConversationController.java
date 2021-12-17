package edu.pjatk.app.socials.chat;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping(value = "/createConversation/{first_user_id}/{second_user_id}")
    public ResponseEntity<?> createConversation(@PathVariable Long first_user_id, @PathVariable Long second_user_id) {
        // TODO handle situation when users have one instance of that conversation
        conversationService.createConversation(first_user_id, second_user_id);
        return new ResponseEntity<>(
                new ResponseMessage("Conversation created!"), HttpStatus.OK
        );
    }

    @GetMapping(value = "/findConversationById/{id}")
    public ResponseEntity<?> findConversationById(@PathVariable Long id) {
        Optional<Conversation> conversation = conversationService.findConversationById(id);
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

    @GetMapping(value = "/findConversationByUserId/{first_user_id}/{second_user_id}")
    public ResponseEntity<?> findConversationByUserId(@PathVariable Long first_user_id, @PathVariable Long second_user_id) {
        Optional<Conversation> conversation = conversationService.findConversationByUserId(first_user_id, second_user_id);
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
        Optional<List<Message>> messages;
        messages = conversationService.getAllMessages(id);
        if (messages.isPresent())
        {
            return new ResponseEntity<>(
                    messages, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no messages!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getRecentMessage/{id}")
    public ResponseEntity<?> getRecentMessage(@PathVariable long id) {
        Optional<Message> message = conversationService.getRecentMessage(id);
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

    @GetMapping(value = "/getAllUserConversations/{userId}")
    public ResponseEntity<?> getAllUserConversations(@PathVariable long userId) {
        Optional<List<Conversation>> conversations = conversationService.getAllUserConversations(userId);
        if (conversations.isPresent())
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
