package edu.pjatk.app.user;

import edu.pjatk.app.email.activation_token.ActivationTokenService;
import edu.pjatk.app.photo.PhotoService;
import edu.pjatk.app.request.PasswordChangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActivationTokenService activationTokenService;
    private final PhotoService photoService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ActivationTokenService activationTokenService,
                       PhotoService photoService, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.activationTokenService = activationTokenService;
        this.photoService = photoService;
        this.passwordEncoder = passwordEncoder;
    }


    public void saveUser(User user){
        userRepository.save(user);
    }

    public void activateUser(User user){
        user.setEnabled(true);
        userRepository.update(user);
    }

    @Transactional
    public void removeCurrentlyLoggedUser(){
        User user = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if (user.getProfile().getPhoto() != null) photoService.removePhoto(user.getProfile().getPhoto());
        activationTokenService.removeActivationTokenByUser(user);
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

}
