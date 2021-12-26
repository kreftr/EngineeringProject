package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FriendResponse {

    private Long id;
    private Long conversationId;
    private String username;
    private String name;
    private String surname;
    private String profilePhoto;

}
