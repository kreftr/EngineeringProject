package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {

    private String messageContent;
    private String authorNickname;
    private String date;
    private String avatar;

}
