package edu.pjatk.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    public void saveUser(User user){
        userRepository.save(user);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    public void activateUser(User user){
        user.setEnabled(true);
        userRepository.update(user);
    }

    public Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

}
