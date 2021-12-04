package edu.pjatk.app.socials.Friends;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService){
        this.friendService = friendService;
    }

    @GetMapping(value = "/findFriendById/{id}")
    public ResponseEntity<?> findFriendById(@PathVariable Long id) {
        Optional<Friend> friend = friendService.findById(id);
        if (friend.isPresent())
        {
            return new ResponseEntity<>(
                    friend, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no friends!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(value = "/addFriend/{first_user_id}/{second_user_id}")
    public ResponseEntity<?> addFriend(@PathVariable Long first_user_id, @PathVariable Long second_user_id) {
        friendService.addFriend(first_user_id, second_user_id);
        return new ResponseEntity<>(
                new ResponseMessage("Message uploaded!"), HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        friendService.deleteFriendById(id);
        return new ResponseEntity<>(
                new ResponseMessage("Message deleted!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/acceptFriend/{id}")
    public ResponseEntity<?> acceptFriend(@PathVariable Long id) {
        friendService.acceptFriend(id);
        return new ResponseEntity<>(
                new ResponseMessage("Friend accepted!"), HttpStatus.OK
        );
    }


}
