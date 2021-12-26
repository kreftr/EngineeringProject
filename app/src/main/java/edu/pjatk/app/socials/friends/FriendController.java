package edu.pjatk.app.socials.friends;

import edu.pjatk.app.response.FriendResponse;
import edu.pjatk.app.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
@CrossOrigin("http://localhost:3000")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService){
        this.friendService = friendService;
    }


    @PostMapping(value = "/addFriend/{user_id}")
    public ResponseEntity<?> addFriend(@PathVariable Long user_id) {

        if (friendService.areUsersFriends(user_id)){
            return new ResponseEntity(
                    new ResponseMessage("Users are already friends!"), HttpStatus.CONFLICT
            );
        }
        else {
            friendService.addFriendByUserId(user_id);
            return new ResponseEntity<>(
                    new ResponseMessage("Friend request sent!"), HttpStatus.OK
            );
        }
    }

    @DeleteMapping(value = "/deleteById/{user_id}")
    public ResponseEntity<?> deleteById(@PathVariable Long user_id) {

        if (friendService.deleteFriendByUserId(user_id)){
            return new ResponseEntity<>(HttpStatus.OK);

        }
        else return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/acceptFriend/{user_id}")
    public ResponseEntity<?> acceptFriend(@PathVariable Long user_id) {

        if (friendService.acceptFriendByUserId(user_id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/getAllFriends/{user_id}")
    public ResponseEntity<?> getAllFriends(@PathVariable Long user_id) {

        List<FriendResponse> friends = friendService.getAllFriendsByUserId(user_id);

        if (!friends.isEmpty())
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

    @GetMapping(value = "/getAllPending")
    public ResponseEntity<?> getAllPending() {

        List<FriendResponse> pendingFriends = friendService.getAllPending();

        if (pendingFriends.size() > 0)
        {
            return new ResponseEntity<>(
                    pendingFriends, HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>(
                    new ResponseMessage("There are no pending friends!"), HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(value = "/getFriendStatus/{friend_id}")
    public ResponseEntity<?> getFriendStatus(@PathVariable Long friend_id) {
        FriendStatus friendStatus =  friendService.getFriendStatus(friend_id);
        return new ResponseEntity<>(friendStatus, HttpStatus.OK);
    }

}
