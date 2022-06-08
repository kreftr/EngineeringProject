package edu.pjatk.app.facebook;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FeedOperations;



public class FacebookController {
    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public FacebookController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

//    Connection<Facebook> connection = ConnectionRepository.findPrimaryConnection(Facebook.class);
//    Facebook facebook = connection.getApi();
//    facebook.FeedOperations().
}
