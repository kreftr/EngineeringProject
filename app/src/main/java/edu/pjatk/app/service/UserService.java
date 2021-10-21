package edu.pjatk.app.service;

import edu.pjatk.app.model.User;
import edu.pjatk.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public void saveUser(User user){
        userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

}
