package edu.pjatk.app.user;

import edu.pjatk.app.WebApp;
import edu.pjatk.app.email.EmailService;
import edu.pjatk.app.email.activation_token.ActivationTokenService;
import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.request.PasswordChangeRequest;
import edu.pjatk.app.socials.chat.Conversation;
import edu.pjatk.app.socials.chat.ConversationService;
import edu.pjatk.app.socials.friends.Friend;
import edu.pjatk.app.socials.friends.FriendService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActivationTokenService activationTokenService;
    private final PhotoService photoService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConversationService conversationService;
    private final FriendService friendService;
    private static final Logger LOGGER = LogManager.getLogger(WebApp.class);


    @Autowired
    public UserService(UserRepository userRepository, ActivationTokenService activationTokenService,
                       PhotoService photoService, BCryptPasswordEncoder passwordEncoder,
                       @Lazy ConversationService conversationService, @Lazy FriendService friendService){
        this.userRepository = userRepository;
        this.activationTokenService = activationTokenService;
        this.photoService = photoService;
        this.passwordEncoder = passwordEncoder;
        this.conversationService = conversationService;
        this.friendService = friendService;
    }


    public void saveUser(User user){
        userRepository.save(user);
        LOGGER.info("Saved user : "+ user.getUsername() + "," + user.getId());
    }

    public void activateUser(User user){
        user.setEnabled(true);
        userRepository.update(user);
    }

    @Transactional
    public void removeUser(User user){

        activationTokenService.removeActivationTokenByUser(user);
        Optional<List<Conversation>> conversations = conversationService.getAllUserConversations(user.getId());

        //Remove user's conversations
        if (conversations.isPresent())
        {
            for (Conversation conversation : conversations.get()) {
                conversationService.removeConversation(conversation);
            }
        }

        //Remove user's friends
        Optional<List<Friend>> friends = friendService.getAllFriendsByUserId(user.getId());
        if (friends.isPresent() && !friends.get().isEmpty()){
            for (Friend friend : friends.get()) friendService.removeFriend(friend);
        }

        //Remove user's categories
        user.getProfile().setCategories(Collections.emptySet());
        userRepository.update(user);

        userRepository.remove(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
        LOGGER.info("Updated user : "+ user.getUsername() + "," + user.getId());
    }

    @Transactional
    public void removeCurrentlyLoggedUser(){
        User user = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if (user.getProfile().getPhoto() != null) photoService.removePhoto(user.getProfile().getPhoto());
        activationTokenService.removeActivationTokenByUser(user);

        //Remove user's conversations
        Optional<List<Conversation>> conversations = conversationService.getAllUserConversations(user.getId());
        if (conversations.isPresent() && !conversations.get().isEmpty())
        {
            for (Conversation conversation : conversations.get()) {
                conversationService.removeConversation(conversation);
            }
        }

        //Remove user's friends
        Optional<List<Friend>> friends = friendService.getAllFriendsByUserId(user.getId());
        if (friends.isPresent() && !friends.get().isEmpty()){
            for (Friend friend : friends.get()) friendService.removeFriend(friend);
        }

        //Remove user's categories
        user.getProfile().setCategories(Collections.emptySet());
        userRepository.update(user);

        userRepository.remove(user);
    }

    //Password change for currently logged user
    @Transactional
    public void changeUserPassword(PasswordChangeRequest passwordRequest){
        User user = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.update(user);
    }

    //Password change for user passed in parameter
    public void changePassword(User user, PasswordChangeRequest passwordChangeRequest){
        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.update(user);
    }

    @Transactional
    public void updateEmailNotification(Boolean isNotificationEnabled){
        User user = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        user.setEmailNotification(isNotificationEnabled);
        userRepository.update(user);
    }

    public Boolean getEmailNotification(){
        User user = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        return user.getEmailNotification();
    }

    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<List<User>> findUsersBySimilarUsername(String username){
        return userRepository.findBySimilarUsername(username);
    }

    public Long getUsersNumber(String username) {
        Optional<Long> number = userRepository.getUsersNumberByUsername(username);
        return number.isPresent() ? number.get() : 0;
    }

    public Optional<List<User>> findUsersWithPagination(String username, int pageNumber, int pageSize) {
        return userRepository.getUsersByUsernameWithPagination(username, pageNumber, pageSize);
    }

}
