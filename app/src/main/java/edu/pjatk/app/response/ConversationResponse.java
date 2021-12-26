package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConversationResponse {

    private Long conversationId;
    private Long userId;
    private String username;
    private String profilePhoto;

}
