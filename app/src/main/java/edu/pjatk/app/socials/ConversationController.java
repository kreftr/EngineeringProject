package edu.pjatk.app.socials;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService){
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<?> addMessage(@RequestPart Message message) {
        String trimmedMessage = message.getMessage().trim();
        if (trimmedMessage.length() > 0)  // disables sending empty messages
        {
            conversationService.addMessage(message);
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

    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestPart long id) {
        conversationService.deleteById(id);
        return new ResponseEntity<>(
                new ResponseMessage("Message deleted!"), HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllMessages(){
        Optional<List<Message>> messages;
        messages = conversationService.getAllMessages();
        if (messages.isPresent())
        {
            return new ResponseEntity<>(
                    messages, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no messages"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> getRecentMessage(){
        Optional<Message> message = conversationService.getRecentMessage();
        if (message.isPresent())
        {
            return new ResponseEntity<>(
                    message, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no messages"), HttpStatus.NOT_FOUND
            );
        }
    }

}
