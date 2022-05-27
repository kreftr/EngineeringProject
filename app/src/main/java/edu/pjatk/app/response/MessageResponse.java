package edu.pjatk.app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {

    private String message;
    private Long conversation_id;
    private String author_nickname;
    private Long author_id;
    private String photoPath;
    private LocalDateTime messageDate;

}
