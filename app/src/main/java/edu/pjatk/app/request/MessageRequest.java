package edu.pjatk.app.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private String message;
    private Long conversation_id;
    private Long author_id;
}
