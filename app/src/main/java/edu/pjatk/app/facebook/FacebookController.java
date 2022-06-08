package edu.pjatk.app.facebook;

import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FeedOperations;
import org.springframework.social.connect.ConnectionRepository;

public class FacebookController {
    Connection<Facebook> connection = ConnectionRepository.findPrimaryConnection(Facebook.class);
    Facebook facebook = connection.getApi();
    facebook.FeedOperations().
}
