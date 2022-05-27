package edu.pjatk.app.request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageRequest {
    private String message;
    private Long conversation_id;
    private String author_nickname;
    private Long author_id;
    private String photoPath;
    private LocalDateTime messageDate;
}
