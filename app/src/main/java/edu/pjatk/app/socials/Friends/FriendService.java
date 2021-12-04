package edu.pjatk.app.socials.Friends;

import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserService userService;

    @Autowired
    public FriendService(FriendRepository friendRepository, UserService userService){
        this.friendRepository = friendRepository;
        this.userService = userService;
    }

    public Optional<Friend> findById(Long id) {
        return friendRepository.findById(id);
    }

    public void addFriend(Long first_user_id, Long second_user_id) {
        Optional<User> firstUser = userService.findUserById(first_user_id);
        Optional<User> secondUser = userService.findUserById(second_user_id);

        if (firstUser.isPresent() && secondUser.isPresent()) {
            Friend friend = new Friend(firstUser.get(), secondUser.get());
            friendRepository.addFriend(friend);
        }
    }

    public void deleteFriendById(Long id) {
        friendRepository.deleteFriendById(id);
    }

    public void acceptFriend(Long id) {
        Optional<Friend> friend = friendRepository.findById(id);
        if (friend.isPresent()) {
            friendRepository.acceptFriend(friend.get());
        }
    }

}
