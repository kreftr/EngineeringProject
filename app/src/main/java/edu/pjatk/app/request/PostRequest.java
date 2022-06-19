package edu.pjatk.app.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class PostRequest {
    @Size(min = 3, max = 24, message = "{validation.registration.username.size}")
    private String title;
    private String text;
}
