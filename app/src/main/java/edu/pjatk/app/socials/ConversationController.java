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

    @PostMapping(value = "/addMessage/{conversation_id}/{text}")
    public ResponseEntity<?> addMessage(@PathVariable Long conversation_id, @PathVariable String text) {
        String trimmedText = text.trim();
        if (trimmedText.length() > 0)  // disables sending empty messages
        {
            conversationService.addMessage(conversation_id, text);
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
    public ResponseEntity<?> getAllMessages(@PathVariable long id){
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
                    new ResponseMessage("There are no messages"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getRecentMessage/{id}")
    public ResponseEntity<?> getRecentMessage(@PathVariable long id){
        Optional<Message> message = conversationService.getRecentMessage(id);
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
