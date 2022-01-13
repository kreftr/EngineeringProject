package edu.pjatk.app.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {

    private Long fileId;
    private String fileName;
    private String fileUrl;
    private Long userId;
    private String username;
    private String profilePhoto;
    private Long size;
    private String uploadDate;

}
