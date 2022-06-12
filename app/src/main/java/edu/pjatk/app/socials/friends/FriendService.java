package edu.pjatk.app.socials.friends;

import edu.pjatk.app.response.FriendResponse;
import edu.pjatk.app.socials.chat.Conversation;
import edu.pjatk.app.socials.chat.ConversationService;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;
    private final ConversationService conversationService;

    @Autowired
    public FriendService(FriendRepository friendRepository, UserService userService,
                         ConversationService conversationService){
        this.friendRepository = friendRepository;
        this.userService = userService;
        this.conversationService = conversationService;
    }


    public void addFriendByUserId(Long id){
        Optional<User> sender = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<User> receiver = userService.findUserById(id);

        if (sender.isPresent() && receiver.isPresent() && !sender.equals(receiver)) {
            Friend friend = new Friend(sender.get(), receiver.get());
            friendRepository.addFriend(friend);
        }
    }

    @Transactional
    public boolean deleteFriendByUserId(Long id) {
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<Friend> friend = friendRepository.getFriendByUserId(loggedUser.get().getId(), id);
        if (loggedUser.isPresent() && friend.isPresent()){
            Optional<Conversation> friendConversation = conversationService.getConversationByUserId(id);
            if (friendConversation.isPresent()) conversationService.removeConversation(friendConversation.get());
            friendRepository.deleteFriend(friend.get());

            Optional<List<Conversation>> conversations = conversationService.getAllUserConversations(loggedUser.get().getId());
            if (conversations.isPresent())
            {
                for (Conversation conversation : conversations.get()) {
                    conversationService.removeConversation(conversation);
                }
            }

            return true;
        }
        else return false;
    }

    public void removeFriend(Friend friend){
        friendRepository.removeFriend(friend);
    }

    @Transactional
    public boolean acceptFriendByUserId(Long id) {
        Optional<User> loggedUser = userService.findUserByUsername(
          SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<Friend> friend = friendRepository.getFriendByUserId(id, loggedUser.get().getId());

        if (loggedUser.isPresent() && friend.isPresent()){
            friend.get().setPending(false);
            friendRepository.acceptFriend(friend.get());
            conversationService.createConversation(id);
            return true;
        }
        else return false;
    }

    public Optional<FriendResponse> getFriendByUserId(Long id){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());

        Optional<Friend> friend = friendRepository.getFriendByUserId(id, loggedUser.get().getId());

        if (friend.isPresent()){
            if (friend.get().getFirstUser().equals(loggedUser)) {
                return Optional.of(
                        new FriendResponse(
                                friend.get().getSecondUser().getId(),
                                1L,
                                friend.get().getSecondUser().getUsername(),
                                friend.get().getSecondUser().getProfile().getName(),
                                friend.get().getSecondUser().getProfile().getSurname(),
                                friend.get().getSecondUser().getProfile().getPhoto().getFileName()
                        )
                );
            }
            else return Optional.of(
                    new FriendResponse(
                            friend.get().getFirstUser().getId(),
                            1L,
                            friend.get().getFirstUser().getUsername(),
                            friend.get().getFirstUser().getProfile().getName(),
                            friend.get().getFirstUser().getProfile().getSurname(),
                            friend.get().getFirstUser().getProfile().getPhoto().getFileName()
                    )
            );
        }
        else return Optional.empty();
    }

    //Returns responses
    public List<FriendResponse> getAllFriendsResponsesByUserId(Long id) {
        Optional<List<Friend>> friendList = friendRepository.getAllFriendsByUserId(id);
        Optional<User> user = userService.findUserById(id);

        if (friendList.isPresent() && friendList.get().size() > 0 && user.isPresent()){
            List<FriendResponse> listToReturn = new ArrayList<>();
            String photoUrl;
            Long conversationId;

            for (Friend f : friendList.get()){
                if (user.get().equals(f.getFirstUser())) {

                    if (conversationService.getConversationByUserId(f.getSecondUser().getId()).isPresent()){
                        conversationId = conversationService.getConversationByUserId(f.getSecondUser().getId()).get().getId();
                    }
                    else conversationId = null;

                    if (f.getSecondUser().getProfile().getPhoto() != null) {
                        photoUrl = f.getSecondUser().getProfile().getPhoto().getFileName();
                    } else {
                        photoUrl = null;
                    }

                    listToReturn.add(
                            new FriendResponse(
                                    f.getSecondUser().getId(),
                                    conversationId,
                                    f.getSecondUser().getUsername(), f.getSecondUser().getProfile().getName(),
                                    f.getSecondUser().getProfile().getSurname(),
                                    photoUrl)
                    );
                }
                else {

                    if (conversationService.getConversationByUserId(f.getFirstUser().getId()).isPresent()){
                        conversationId = conversationService.getConversationByUserId(f.getFirstUser().getId()).get().getId();
                    }
                    else conversationId = null;

                    if (f.getFirstUser().getProfile().getPhoto() != null) {
                        photoUrl = f.getFirstUser().getProfile().getPhoto().getFileName();
                    } else {
                        photoUrl = null;
                    }

                    listToReturn.add(
                            new FriendResponse(
                                    f.getFirstUser().getId(),
                                    conversationId,
                                    f.getFirstUser().getUsername(), f.getFirstUser().getProfile().getName(),
                                    f.getFirstUser().getProfile().getSurname(),
                                    photoUrl)
                    );
                }
            }
            return listToReturn;
        }
        else return Collections.emptyList();
    }

    //Returns entity models
    public Optional<List<Friend>> getAllFriendsByUserId(Long id){
        return friendRepository.getAllFriendsByUserId(id);
    }

    public List<FriendResponse> getAllPending(){
        Optional<User> user = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
        Optional<List<Friend>> pendingList = friendRepository.getAllPending(user.get().getId());

        if (pendingList.isPresent() && pendingList.get().size() > 0 && user.isPresent()){
            List<FriendResponse> listToReturn = new ArrayList<>();
            String photoUrl;
            for (Friend f : pendingList.get()){
                if (f.getFirstUser().getProfile().getPhoto() != null) {
                    photoUrl = f.getFirstUser().getProfile().getPhoto().getFileName();
                } else {
                    photoUrl = null;
                }

                listToReturn.add(
                        new FriendResponse(
                                f.getFirstUser().getId(),
                                1L,
                                f.getFirstUser().getUsername(), f.getFirstUser().getProfile().getName(),
                                f.getFirstUser().getProfile().getSurname(),
                                photoUrl
                                )
                );
            }
            return listToReturn;
        }
        else return Collections.emptyList();
    }

    public FriendStatus getFriendStatus(Long friend_id){
        Optional<User> loggedUser = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        Optional<List<Friend>> friendsList = friendRepository.getAll(loggedUser.get().getId());

        if (friendsList.isPresent()) {
            for (Friend f : friendsList.get()){
                if (f.getFirstUser().getId() == friend_id || f.getSecondUser().getId() == friend_id){
                    if (f.getPending()) return FriendStatus.PENDING;
                    else return FriendStatus.FRIEND;
                }
            }
        }
        return FriendStatus.NOT_FRIEND;
    }

    public boolean areUsersFriends(Long user_id){
        Long logged_user_id = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).get().getId();
        return friendRepository.getFriendByUserId(user_id, logged_user_id).isPresent();
    }

}
