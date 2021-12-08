package edu.pjatk.app.socials.Friends;

import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/friends")
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

    @DeleteMapping(value = "/deleteById/{friend_id}")
    public ResponseEntity<?> deleteById(@PathVariable Long friend_id) {
        friendService.deleteFriendById(friend_id);
        return new ResponseEntity<>(
                new ResponseMessage("Message deleted!"), HttpStatus.OK
        );
    }

    @PostMapping(value = "/acceptFriend/{friend_id}")
    public ResponseEntity<?> acceptFriend(@PathVariable Long friend_id) {
        friendService.acceptFriend(friend_id);
        return new ResponseEntity<>(
                new ResponseMessage("Friend accepted!"), HttpStatus.OK
        );
    }

    // TODO przetestuj
    @GetMapping(value = "/getAllFriends/{friend_id}")
    public ResponseEntity<?> getAllFriends(@PathVariable Long friend_id) {
        Optional<List<Friend>> friends = friendService.getAllFriends(friend_id);
        if (friends.isPresent())
        {
            return new ResponseEntity<>(
                    friends, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no friends!"), HttpStatus.NOT_FOUND
            );
        }
    }

    // TODO przetestuj
    @GetMapping(value = "/getAllPending/{friend_id}")
    public ResponseEntity<?> getAllPending(@PathVariable Long friend_id) {
        Optional<List<Friend>> pending_friends = friendService.getAllPending(friend_id);
        if (pending_friends.isPresent())
        {
            return new ResponseEntity<>(
                    pending_friends, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no pending friends!"), HttpStatus.NOT_FOUND
            );
        }
    }

}
